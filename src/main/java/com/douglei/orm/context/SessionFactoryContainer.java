package com.douglei.orm.context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.douglei.aop.ProxyBeanContainer;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.mapping.MappingContainer;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * jdb-orm 的SessionFactory容器
 * @author DougLei
 */
public final class SessionFactoryContainer {
	private SessionFactoryContainer(){}
	private static SessionFactoryContainer singleton = new SessionFactoryContainer();
	public static SessionFactoryContainer getSingleton() {
		return singleton;
	}
	
	// --------------------------------------------------------------------------------------------
	// 注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 通过配置文件, 注册SessionFactory实例
	 * @param file
	 * @param dataSource 可为空
	 * @param mappingContainer 可为空
	 * @return
	 * @throws IdRepeatedException 
	 */
	public RegistrationResult registerByFile(String file, ExternalDataSource dataSource, MappingContainer mappingContainer) throws IdRepeatedException {
		InputStream input = SessionFactoryContainer.class.getClassLoader().getResourceAsStream(file);
		return registerByInputStream(input, dataSource, mappingContainer, false);
	}
	
	/**
	 * 通过配置文件的内容字符串, 注册SessionFactory实例
	 * @param content
	 * @param dataSource
	 * @param mappingContainer
	 * @return 
	 * @throws IdRepeatedException 
	 */
	public RegistrationResult registerByContent(String content, ExternalDataSource dataSource, MappingContainer mappingContainer) throws IdRepeatedException {
		return registerByInputStream(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), dataSource, mappingContainer, false);
	}
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource
	 * @param mappingContainer
	 * @return 
	 * @throws IdRepeatedException 
	 */
	public RegistrationResult registerByStream(InputStream input, ExternalDataSource dataSource, MappingContainer mappingContainer) throws IdRepeatedException {
		return registerByInputStream(input, dataSource, mappingContainer, false);
	}
	
	
	/**
	 * 通过配置文件内容流, 注册SessionFactory实例
	 * @param input
	 * @param dataSource 可为空
	 * @param mappingContainer 可为空
	 * @param scanAll
	 * @param transactionComponentPackages
	 * @return
	 * @throws IdRepeatedException 
	 */
	public RegistrationResult registerByInputStream(InputStream input, ExternalDataSource dataSource, MappingContainer mappingContainer, boolean scanAll, String... transactionComponentPackages) throws IdRepeatedException {
		Configuration configuration = new Configuration();
		configuration.setExternalDataSource(dataSource);
		configuration.setMappingContainer(mappingContainer);
		SessionFactory sessionFactory = configuration.buildSessionFactory(input);
		
		// 根据包路径扫描事务组件
		if(transactionComponentPackages.length > 0) {
			TransactionAnnotationScanner.scan(scanAll, transactionComponentPackages).forEach(entity -> {
				ProxyBeanContainer.createAndAddProxy(entity.getClazz(), new TransactionProxyInterceptor(entity.getMethods()));
			});
		}
		return register(sessionFactory);
	}
	
	/**
	 * 注册SessionFactory实例
	 * @param sessionFactory
	 * @return
	 * @throws IdRepeatedException 
	 */
	public synchronized RegistrationResult register(SessionFactory sessionFactory) throws IdRepeatedException {
		return SessionFactoryContext.register(sessionFactory);
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactory get() {
		return SessionFactoryContext.get();
	}
	
	/**
	 * 获取指定id的SessionFactory
	 * @param id
	 * @return
	 */
	public SessionFactory get(String id) {
		SessionFactoryIdHolder.setId(id);
		return SessionFactoryContext.get();
	}
	
	/**
	 * 移除SessionFactory
	 * @param id
	 * @param destroy 是否在移除的同时销毁SessionFactory
	 * @return 
	 */
	public synchronized SessionFactory remove(String id, boolean destroy) {
		return SessionFactoryContext.remove(id, destroy);
	}
}
