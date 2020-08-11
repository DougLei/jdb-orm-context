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
	private static byte instanceCount = 0;// 实例化次数
	
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
	// 【默认数据源】注册默认的SessionFactory
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
		InputStream input = SessionFactoryRegister.class.getClassLoader().getResourceAsStream(configurationFile);
		if(input == null) {
			throw new SessionFactoryRegistrationException("不存在路径为["+configurationFile+"]的配置文件");
		}
		SessionFactory sessionFactory = buildSessionFactory(input, dataSource, mappingStore);
		
		// 根据包路径扫描事务组件
		if(transactionComponentPackages.length > 0) {
			List<TransactionComponentEntity> transactionComponentEntities = TransactionAnnotationMemoryUsage.scanTransactionComponent(searchAll, transactionComponentPackages);
			for (TransactionComponentEntity transactionComponentEntity : transactionComponentEntities) {
				ProxyBeanContext.createAndAddProxy(transactionComponentEntity.getTransactionComponentClass(), new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods()));
			}
		}
		
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		registerDefaultSessionFactory = true;
		return sessionFactory;
	}
	
	/**
	 * 注册指定的 SessionFactory实例
	 * @param sessionFactory
	 * @return
	 */
	public synchronized SessionFactory registerDefaultSessionFactory(SessionFactory sessionFactory) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		registerDefaultSessionFactory = true;
		return sessionFactory;
	}
	
	// --------------------------------------------------------------------------------------------
	// 【多数据源】注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 使用指定的配置文件path注册jdb-orm SessionFactory实例
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
	 * 使用指定的配置文件content注册jdb-orm SessionFactory实例
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
	 * 使用指定的配置文件input流注册jdb-orm SessionFactory实例
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
	
	/**
	 * 注册指定的 SessionFactory实例
	 * @param sessionFactory
	 * @return 
	 */
	public synchronized SessionFactory registerSessionFactory(SessionFactory sessionFactory) {
		if(registerDefaultSessionFactory) {
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
	 * 获取默认的SessionFactory
	 * @return
	 */
	public SessionFactory getDefaultSessionFactory() {
		if(registerDefaultSessionFactory) {
			return SessionFactoryContext.getDefaultSessionFactory();
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 获取SessionFactory
	 * 当只有默认数据源时, 该方法等效于 @see {@link SessionFactoryRegister#getDefaultSessionFactory()}
	 * 当有多个数据源, 调用该方法前需要先设置数据源id @see {@link MultiSessionFactoryHandler#setSessionFactoryId(String)}, 或使用 @see {@link SessionFactoryRegister#getSessionFactory(String)}方法
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		if(registerDefaultSessionFactory) {
			return SessionFactoryContext.getSessionFactory();
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 获取SessionFactory
	 * @param sessionFactoryId 指定数据源id
	 * @return
	 */
	public SessionFactory getSessionFactory(String sessionFactoryId) {
		if(registerDefaultSessionFactory) {
			MultiSessionFactoryHandler.setSessionFactoryId(sessionFactoryId);
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
}
