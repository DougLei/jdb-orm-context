package com.douglei.orm.context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.configuration.environment.mapping.store.MappingStore;
import com.douglei.orm.configuration.impl.ConfigurationImpl;
import com.douglei.orm.context.exception.SessionFactoryRegistrationException;
import com.douglei.orm.context.exception.TooManyInstanceException;
import com.douglei.orm.context.transaction.component.TransactionAnnotationScanner;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * jdb-orm 的SessionFactory注册器
 * <p><b>注意: 该类只支持创建一个实例</b></p>
 * @author DougLei
 */
public final class SessionFactoryRegister {
	private static byte instanceCount = 0;// 实例化次数
	
	public SessionFactoryRegister() throws TooManyInstanceException {
		if(instanceCount > 0) 
			throw new TooManyInstanceException(SessionFactoryRegister.class.getName() + ", 只能创建一个实例, 请妥善处理创建出的实例, 保证其在项目中处于全局范围");
		instanceCount=1;
	}
	
	// --------------------------------------------------------------------------------------------
	// 注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 通过配置文件, 注册SessionFactory实例
	 * @param file
	 * @param dataSource 可为空
	 * @param mappingStore 可为空
	 * @return
	 */
	public SessionFactory registerSessionFactoryByFile(String file, ExternalDataSource dataSource, MappingStore mappingStore) {
		InputStream input = SessionFactoryRegister.class.getClassLoader().getResourceAsStream(file);
		if(input == null) 
			throw new SessionFactoryRegistrationException("不存在["+file+"]的配置文件");
		return registerSessionFactory(input, dataSource, mappingStore, false);
	}
	
	/**
	 * 通过配置文件的内容字符串, 注册SessionFactory实例
	 * @param content
	 * @param dataSource
	 * @param mappingStore
	 * @return 
	 */
	public SessionFactory registerSessionFactoryByContent(String content, ExternalDataSource dataSource, MappingStore mappingStore) {
		return registerSessionFactory(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), dataSource, mappingStore, false);
	}
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource
	 * @param mappingStore
	 * @return 
	 */
	public SessionFactory registerSessionFactoryByInput(InputStream input, ExternalDataSource dataSource, MappingStore mappingStore) {
		return registerSessionFactory(input, dataSource, mappingStore, false);
	}
	
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource 可为空
	 * @param mappingStore 可为空
	 * @param searchAll
	 * @param transactionComponentPackages
	 * @return
	 */
	public SessionFactory registerSessionFactory(InputStream input, ExternalDataSource dataSource, MappingStore mappingStore, boolean searchAll, String... transactionComponentPackages) {
		Configuration configuration = new ConfigurationImpl(input);
		configuration.setExternalDataSource(dataSource);
		configuration.setMappingStore(mappingStore);
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		
		// 根据包路径扫描事务组件
		if(transactionComponentPackages.length > 0) {
			List<TransactionComponentEntity> transactionComponentEntities = TransactionAnnotationScanner.scan(searchAll, transactionComponentPackages);
			for (TransactionComponentEntity transactionComponentEntity : transactionComponentEntities) {
				ProxyBeanContext.createAndAddProxy(transactionComponentEntity.getTransactionComponentClass(), new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods()));
			}
		}
		
		return registerSessionFactory(sessionFactory);
	}
	
	/**
	 * 直接注册SessionFactory实例
	 * @param sessionFactory
	 * @return
	 */
	public SessionFactory registerSessionFactory(SessionFactory sessionFactory) {
		if(SessionFactoryContext.registerSessionFactory(sessionFactory) == 2)
			registerMultipleSessionFactories = true;
		return sessionFactory;
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return SessionFactoryContext.getSessionFactory();
	}
	
	/**
	 * 获取SessionFactory
	 * @param sessionFactoryId 指定SessionFactory id
	 * @return
	 */
	public SessionFactory getSessionFactory(String sessionFactoryId) {
		SessionFactoryIdHolder.setId(sessionFactoryId);
		return SessionFactoryContext.getSessionFactory();
	}
	
	/**
	 * 移除SessionFactory, 具体需不需要销毁由调用方决定
	 * @param sessionFactoryId
	 * @return
	 */
//	public synchronized SessionFactory removeSessionFactory(String sessionFactoryId) {
//		
//	}
	
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 */
	public synchronized void destroySessionFactory(String sessionFactoryId) {
		if(registerMultipleSessionFactories) 
			registerMultipleSessionFactories = SessionFactoryContext.destroySessionFactory(sessionFactoryId);
		throw new UnRegisterMultipleSessionFactoriesException("当前程序未注册多个SessionFactory, 禁止进行destroySessionFactory操作");
	}
}
