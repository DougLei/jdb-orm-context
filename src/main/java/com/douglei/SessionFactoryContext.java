package com.douglei;

import java.util.HashMap;
import java.util.Map;

import com.douglei.def.mapping.DefaultMapping;
import com.douglei.exception.DefaultSessionFactoryExistsException;
import com.douglei.exception.NotExistsSessionFactoryException;
import com.douglei.exception.RepeatedSessionFactoryException;
import com.douglei.sessionfactory.SessionFactory;
import com.douglei.utils.StringUtil;

/**
 * jdb-orm SessionFactory 上下文
 * @author DougLei
 */
class SessionFactoryContext {
	private static final short dynamicSessionFactoryCount = 8;// 动态的SessionFactory数量
	private static SessionFactory DEFAULT_JDB_ORM_SESSION_FACTORY;// 默认的jdb-orm SessionFactory对象
	private static Map<String, SessionFactory> JDB_ORM_SESSION_FACTORY_MAPPING;// jdb-orm SessionFactory映射
	private static Map<String, DefaultMapping> DEFAULT_MAPPINGS = new HashMap<String, DefaultMapping>(dynamicSessionFactoryCount+1);// 默认的映射集合
	
	// --------------------------------------------------------------------------------------------
	// 注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 注册默认的SessionFactory对象
	 * @param sessionFactory
	 * @throws DefaultSessionFactoryExistsException
	 */
	static void registerDefaultSessionFactory(SessionFactory sessionFactory) throws DefaultSessionFactoryExistsException{
		DEFAULT_JDB_ORM_SESSION_FACTORY = sessionFactory;
	}
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 */
	static void registerSessionFactory(SessionFactory sessionFactory) {
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {
			JDB_ORM_SESSION_FACTORY_MAPPING = new HashMap<String, SessionFactory>(dynamicSessionFactoryCount);
		}else {
			if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactory.getId())) {
				throw new RepeatedSessionFactoryException(sessionFactory.getId());
			}
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
		return DEFAULT_JDB_ORM_SESSION_FACTORY;
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactory getSessionFactory() {
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {// 没有动态添加SessionFactory时, 返回默认的SessionFactory
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
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
		sessionFactory = null;
	}
	
	// --------------------------------------------------------------------------------------------
	// 添加默认映射
	// --------------------------------------------------------------------------------------------
	/**
	 * <pre>
	 * 	给指定的SessionFactory 添加默认映射
	 * 	举例理解: 添加当前项目必须要的表对应的映射
	 * </pre>
	 * @param defaultMapping
	 */
	static void addDefaultMapping(DefaultMapping defaultMapping) {
		SessionFactory sessionFactory = getSessionFactory();
		
		
	}
}
