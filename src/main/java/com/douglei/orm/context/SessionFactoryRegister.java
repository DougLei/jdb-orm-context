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
import com.douglei.orm.context.exception.DuplicateRegisterSessionFactoryException;
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
	private static SessionFactoryRegistrationFeature DEFAULT_FEATURE = new SessionFactoryRegistrationFeature(true, true);
	
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
	 * @param feature 将SessionFactory注册后, 其在容器中具有的特性; 可以传入null, 在传入null时, 会使用默认的特性
	 * @return
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	public void registerSessionFactoryByFile(String file, ExternalDataSource dataSource, MappingStore mappingStore, SessionFactoryRegistrationFeature feature) throws DuplicateRegisterSessionFactoryException {
		InputStream input = SessionFactoryRegister.class.getClassLoader().getResourceAsStream(file);
		if(input == null) 
			throw new SessionFactoryRegistrationException("不存在["+file+"]的配置文件");
		registerSessionFactory(input, dataSource, mappingStore, feature, false);
	}
	
	/**
	 * 通过配置文件的内容字符串, 注册SessionFactory实例
	 * @param content
	 * @param dataSource
	 * @param mappingStore
	 * @param feature 将SessionFactory注册后, 其在容器中具有的特性; 可以传入null, 在传入null时, 会使用默认的特性
	 * @return 
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	public void registerSessionFactoryByContent(String content, ExternalDataSource dataSource, MappingStore mappingStore, SessionFactoryRegistrationFeature feature) throws DuplicateRegisterSessionFactoryException {
		registerSessionFactory(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), dataSource, mappingStore, feature, false);
	}
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource
	 * @param mappingStore
	 * @param feature 将SessionFactory注册后, 其在容器中具有的特性; 可以传入null, 在传入null时, 会使用默认的特性
	 * @return 
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	public void registerSessionFactoryByInput(InputStream input, ExternalDataSource dataSource, MappingStore mappingStore, SessionFactoryRegistrationFeature feature) throws DuplicateRegisterSessionFactoryException {
		registerSessionFactory(input, dataSource, mappingStore, feature, false);
	}
	
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource 可为空
	 * @param mappingStore 可为空
	 * @param feature 将SessionFactory注册后, 其在容器中具有的特性; 可以传入null, 在传入null时, 会使用默认的特性
	 * @param searchAll
	 * @param transactionComponentPackages
	 * @return
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	public void registerSessionFactory(InputStream input, ExternalDataSource dataSource, MappingStore mappingStore, SessionFactoryRegistrationFeature feature, boolean searchAll, String... transactionComponentPackages) throws DuplicateRegisterSessionFactoryException {
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
		
		registerSessionFactory(sessionFactory, feature);
	}
	
	/**
	 * 注册SessionFactory实例
	 * @param sessionFactory
	 * @param feature 将SessionFactory注册后, 其在容器中具有的特性; 可以传入null, 在传入null时, 会使用默认的特性
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	public void registerSessionFactory(SessionFactory sessionFactory, SessionFactoryRegistrationFeature feature) throws DuplicateRegisterSessionFactoryException {
		if(feature == null)
			feature = DEFAULT_FEATURE;
		SessionFactoryContext.register(sessionFactory, feature);
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactoryWrapper getSessionFactory() {
		return SessionFactoryContext.get();
	}
	
	/**
	 * 获取SessionFactory
	 * @param id 指定SessionFactory id
	 * @return
	 */
	public SessionFactoryWrapper getSessionFactory(String id) {
		SessionFactoryIdHolder.setId(id);
		return SessionFactoryContext.get();
	}
	
	/**
	 * 移除SessionFactory
	 * @param id
	 * @return 被移除的SessionFactoryWrapper
	 */
	public synchronized SessionFactoryWrapper remove(String id) {
		return SessionFactoryContext.remove(id);
	}
	
	/**
	 * 销毁SessionFactory
	 * @param id
	 */
	public synchronized void destroySessionFactory(String id) {
		SessionFactoryContext.destroy(id);
	}
}
