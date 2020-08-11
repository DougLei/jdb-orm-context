package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.context.exception.NotExistsSessionFactoryException;
import com.douglei.orm.context.exception.ProhibitDestroyDefaultSessionFactoryException;
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
		String sessionFactoryId = sessionFactory.getId();
		if(!DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
			if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {
				JDB_ORM_SESSION_FACTORY_MAPPING = new HashMap<String, SessionFactory>(8);
			}
			if(JDB_ORM_SESSION_FACTORY_MAPPING.isEmpty() || !JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)){
				JDB_ORM_SESSION_FACTORY_MAPPING.put(sessionFactoryId, sessionFactory);
			}
		}
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
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null || JDB_ORM_SESSION_FACTORY_MAPPING.isEmpty()) {// 没有动态添加SessionFactory时, 返回默认的SessionFactory
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
		}
		String sessionFactoryId = MultiSessionFactoryHandler.getSessionFactoryId();
		if(StringUtil.isEmpty(sessionFactoryId)) {
			throw new NullPointerException("注册了多个SessionFactory, 在获取SessionFactory时, 必须指定要获取的数据源id");
		}
		
		if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			return JDB_ORM_SESSION_FACTORY_MAPPING.get(sessionFactoryId);
		}else if(DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
		}
		throw new NotExistsSessionFactoryException(sessionFactoryId);
	}
	
	// --------------------------------------------------------------------------------------------
	// 销毁SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 * @return 是否还存在其他数据源
	 */
	static boolean destroySessionFactory(String sessionFactoryId) {
		if(JDB_ORM_SESSION_FACTORY_MAPPING != null && JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			JDB_ORM_SESSION_FACTORY_MAPPING.remove(sessionFactoryId).destroy();
			return JDB_ORM_SESSION_FACTORY_MAPPING.size() > 0;
		}else {
			if(DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId))
				throw new ProhibitDestroyDefaultSessionFactoryException(sessionFactoryId);
			throw new NotExistsSessionFactoryException(sessionFactoryId);
		}
	}
}
