package com.douglei;

import java.util.HashMap;
import java.util.Map;

import com.douglei.exception.NotExistsSessionFactoryException;
import com.douglei.exception.RepeatedSessionFactoryException;
import com.douglei.func.mapping.FunctionMapping;
import com.douglei.sessionfactory.SessionFactory;
import com.douglei.utils.StringUtil;

/**
 * jdb-orm SessionFactory 上下文
 * @author DougLei
 */
class SessionFactoryContext {
	private static SessionFactory DEFAULT_JDB_ORM_SESSION_FACTORY;// 默认的jdb-orm SessionFactory对象
	private static Map<String, SessionFactory> JDB_ORM_SESSION_FACTORY_MAPPING;// jdb-orm SessionFactory映射
	
	// --------------------------------------------------------------------------------------------
	// 注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 注册默认的SessionFactory对象
	 * @param sessionFactory
	 */
	static void registerDefaultSessionFactory(SessionFactory sessionFactory) {
		DEFAULT_JDB_ORM_SESSION_FACTORY = sessionFactory;
	}
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 */
	static void registerSessionFactory(SessionFactory sessionFactory) {
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {
			JDB_ORM_SESSION_FACTORY_MAPPING = new HashMap<String, SessionFactory>(8);
		}else if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactory.getId())) {
			throw new RepeatedSessionFactoryException(sessionFactory.getId());
		}
		JDB_ORM_SESSION_FACTORY_MAPPING.put(sessionFactory.getId(), sessionFactory);
	}
	
	// --------------------------------------------------------------------------------------------
	// 获取SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取默认的SessionFactory对象
	 * @return
	 */
	static SessionFactory getDefaultSessionFactory() {
		if(DEFAULT_JDB_ORM_SESSION_FACTORY == null) {
			throw new NullPointerException("DefaultSessionFactory不能为空, 请务必registerDefaultSessionFactory()");
		}
		return DEFAULT_JDB_ORM_SESSION_FACTORY;
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactory getSessionFactory() {
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {// 没有动态添加SessionFactory时, 返回默认的SessionFactory
			return getDefaultSessionFactory();
		}
		String sessionFactoryId4CurrentThread = SessionFactoryId4CurrentThread.getSessionFactoryId4CurrentThread();
		if(StringUtil.isEmpty(sessionFactoryId4CurrentThread)) {
			throw new NullPointerException("注册了多个SessionFactory时, 在获取SessionFactory时, 必须使用"+SessionFactoryId4CurrentThread.class.getName()+", 设置当前线程要使用的SessionFactory的Id");
		}
		
		if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId4CurrentThread)) {
			return JDB_ORM_SESSION_FACTORY_MAPPING.get(sessionFactoryId4CurrentThread);
		}else if(DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId4CurrentThread)) {
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
		}
		throw new NotExistsSessionFactoryException(sessionFactoryId4CurrentThread);
	}
	
	// --------------------------------------------------------------------------------------------
	// 销毁SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 */
	static void destroySessionFactory(String sessionFactoryId) {
		SessionFactory sessionFactory = JDB_ORM_SESSION_FACTORY_MAPPING.remove(sessionFactoryId);
		if(sessionFactory == null) {
			throw new NotExistsSessionFactoryException(sessionFactoryId);
		}
		sessionFactory.destroy();
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作FunctionMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 给SessionFactory添加FunctionMapping
	 * @param functionMapping
	 */
	static void addFunctionMapping(FunctionMapping functionMapping) {
		FunctionMappingContext.registerFunctionMapping(functionMapping);
		SessionFactory sessionFactory = getSessionFactory();
		if(SessionFactoryAndFunctionMappingLinkContext.addLink(sessionFactory.getId(), functionMapping)) {
			sessionFactory.dynamicBatchAddMapping(functionMapping.getMappings());
		}
	}
	
	/**
	 * 给SessionFactory添加FunctionMapping
	 * @param functionMappingClassName
	 */
	static void addFunctionMapping(String functionMappingClassName) {
		SessionFactory sessionFactory = getSessionFactory();
		if(SessionFactoryAndFunctionMappingLinkContext.addLink(sessionFactory.getId(), functionMappingClassName)) {
			sessionFactory.dynamicBatchAddMapping(FunctionMappingContext.getFunctionMapping(functionMappingClassName).getMappings());
		}
	}
	
	/**
	 * 从SessionFactory移除FunctionMapping
	 * @param functionMappingClassName
	 */
	static void removeFunctionMapping(String functionMappingClassName) {
		SessionFactory sessionFactory = getSessionFactory();
		if(SessionFactoryAndFunctionMappingLinkContext.removeLink(sessionFactory.getId(), functionMappingClassName)) {
			sessionFactory.dynamicBatchRemoveMapping(FunctionMappingContext.getFunctionMapping(functionMappingClassName).getMappingCodes());
		}
	}
}
