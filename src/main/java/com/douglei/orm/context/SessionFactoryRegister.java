package com.douglei.orm.context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.configuration.environment.mapping.store.MappingStore;
import com.douglei.orm.configuration.impl.xml.XmlConfiguration;
import com.douglei.orm.context.exception.DefaultSessionFactoryExistsException;
import com.douglei.orm.context.exception.SessionFactoryRegistrationException;
import com.douglei.orm.context.exception.TooManyInstanceException;
import com.douglei.orm.context.exception.UnRegisterDefaultSessionFactoryException;
import com.douglei.orm.context.exception.UnRegisterMultipleSessionFactoryException;
import com.douglei.orm.context.transaction.component.TransactionAnnotationMemoryUsage;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * jdb-orm 的SessionFactory注册器
 * <p><b>注意: 该类只支持创建一个实例</b></p>
 * @author DougLei
 */
public final class SessionFactoryRegister {
	private boolean registerDefaultSessionFactory;// 是否注册过默认的SessionFactory
	private boolean registerMultipleSessionFactory;// 是否注册过多个SessionFactory
	private static short instanceCount = 0;// 实例化次数
	
	public SessionFactoryRegister() {
		if(instanceCount > 0) {
			throw new TooManyInstanceException(SessionFactoryRegister.class.getName() + ", 只能创建一个实例, 请妥善处理创建出的实例, 保证其在项目中处于全局范围");
		}
		instanceCount=1;
	}
	
	/**
	 * 
	 * @param configurationFile
	 * @param dataSource 可为空
	 * @param mappingStore 可为空
	 * @return
	 */
	private SessionFactory buildSessionFactory(InputStream configurationFile, ExternalDataSource dataSource, MappingStore mappingStore) {
		Configuration configuration = new XmlConfiguration(configurationFile);
		configuration.setExternalDataSource(dataSource);
		configuration.setMappingStore(mappingStore);
		return configuration.buildSessionFactory();
	}
	
	// --------------------------------------------------------------------------------------------
	// 【必须的数据源】注册默认的SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 使用指定的配置文件注册默认的jdb-orm Configuration实例
	 * @param configurationFile
	 * @param dataSource 可为空
	 * @param mappingStore 可为空
	 * @param searchAll
	 * @param transactionComponentPackages
	 * @return
	 */
	public synchronized SessionFactory registerDefaultSessionFactory(String configurationFile, ExternalDataSource dataSource, MappingStore mappingStore, boolean searchAll, String... transactionComponentPackages) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		SessionFactory sessionFactory = buildSessionFactory(SessionFactoryRegister.class.getClassLoader().getResourceAsStream(configurationFile), dataSource, mappingStore);
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		
		scanTransactionComponent(searchAll, transactionComponentPackages);
		registerDefaultSessionFactory = true;
		return sessionFactory;
	}
	
	/**
	 * 根据包路径扫描事务组件
	 * @param searchAll
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 */
	private void scanTransactionComponent(boolean searchAll, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			List<TransactionComponentEntity> transactionComponentEntities = TransactionAnnotationMemoryUsage.scanTransactionComponent(searchAll, transactionComponentPackages);
			for (TransactionComponentEntity transactionComponentEntity : transactionComponentEntities) {
				ProxyBeanContext.createAndAddProxy(transactionComponentEntity.getTransactionComponentClass(), new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods()));
			}
		}
	}

	// --------------------------------------------------------------------------------------------
	// 【多数据源】注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 【多数据源】使用指定的配置文件path注册jdb-orm SessionFactory实例
	 * @param configurationFile
	 * @param dataSource
	 * @param mappingCacheStore
	 * @return 
	 */
	public SessionFactory registerSessionFactoryByConfigurationFile(String configurationFile, ExternalDataSource dataSource, MappingStore mappingCacheStore) {
		if(registerDefaultSessionFactory) {
			InputStream input = SessionFactoryRegister.class.getClassLoader().getResourceAsStream(configurationFile);
			if(input == null) {
				throw new SessionFactoryRegistrationException("不存在路径为["+configurationFile+"]的配置文件");
			}
			return registerSessionFactoryByConfigurationInputStream(input, dataSource, mappingCacheStore);
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 【多数据源】使用指定的配置文件content注册jdb-orm SessionFactory实例
	 * @param configurationContent
	 * @param dataSource
	 * @param mappingCacheStore
	 * @return 
	 */
	public SessionFactory registerSessionFactoryByConfigurationContent(String configurationContent, ExternalDataSource dataSource, MappingStore mappingCacheStore) {
		if(registerDefaultSessionFactory) {
			return registerSessionFactoryByConfigurationInputStream(new ByteArrayInputStream(configurationContent.getBytes(StandardCharsets.UTF_8)), dataSource, mappingCacheStore);
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 【多数据源】使用指定的配置文件input流注册jdb-orm SessionFactory实例
	 * @param configurationFile
	 * @param dataSource
	 * @param mappingCacheStore
	 * @return 
	 */
	public synchronized SessionFactory registerSessionFactoryByConfigurationInputStream(InputStream configurationFile, ExternalDataSource dataSource, MappingStore mappingCacheStore) {
		if(registerDefaultSessionFactory) {
			SessionFactory sessionFactory = buildSessionFactory(configurationFile, dataSource, mappingCacheStore);
			SessionFactoryContext.registerSessionFactory(sessionFactory);
			registerMultipleSessionFactory = true;
			return sessionFactory;
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		if(registerDefaultSessionFactory) {
			return SessionFactoryContext.getSessionFactory();
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 */
	public synchronized void destroySessionFactory(String sessionFactoryId) {
		if(registerMultipleSessionFactory) {
			registerMultipleSessionFactory = SessionFactoryContext.destroySessionFactory(sessionFactoryId);
		}
		throw new UnRegisterMultipleSessionFactoryException("没有注册多个SessionFactory, 无法进行destroySessionFactory操作");
	}
	
	/**
	 * 设置要操作的sessionFactoryId
	 * @param sessionFactoryId
	 */
	public SessionFactoryRegister setSessionFactoryId(String sessionFactoryId) {
		if(registerMultipleSessionFactory) {
			SessionFactoryId4CurrentThread.setSessionFactoryId4CurrentThread(sessionFactoryId);
		}
		return this;
	}
}
