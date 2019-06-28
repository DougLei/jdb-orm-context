package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.context.exception.NotExistsSessionFactoryException;
import com.douglei.orm.context.exception.RepeatedSessionFactoryException;
import com.douglei.orm.context.exception.UnRegisterDefaultSessionFactoryException;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.utils.StringUtil;

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
			throw new UnRegisterDefaultSessionFactoryException();
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
		String sessionFactoryId = SessionFactoryId4CurrentThread.getSessionFactoryId4CurrentThread();
		if(StringUtil.isEmpty(sessionFactoryId)) {
			throw new NullPointerException("注册了多个SessionFactory(即多数据源)时, 在获取SessionFactory时, 必须使用["+SessionFactoryRegister.class.getName()+"]中的setSessionFactoryId(...)方法, 或["+SessionFactoryId4CurrentThread.class.getName()+"]中的setSessionFactoryId4CurrentThread(...)方法, 指定要获取的数据源id");
		}
		
		if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			return JDB_ORM_SESSION_FACTORY_MAPPING.get(sessionFactoryId);
		}else if(getDefaultSessionFactory().getId().equals(sessionFactoryId)) {
			return getDefaultSessionFactory();
		}
		throw new NotExistsSessionFactoryException(sessionFactoryId);
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
}
