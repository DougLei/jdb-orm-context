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
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 * @return 0 传入的SessionFactory已经注册过, 1 将传入的SessionFactory注册为默认数据源, 2 将传入的SessionFactory注册到数据源Map集合中
	 */
	static byte registerSessionFactory(SessionFactory sessionFactory) {
		if(DEFAULT_JDB_ORM_SESSION_FACTORY == null) {
			DEFAULT_JDB_ORM_SESSION_FACTORY = sessionFactory;
			return 1;
		}else {
			String sessionFactoryId = sessionFactory.getId();
			if(!DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
				if(JDB_ORM_SESSION_FACTORY_MAPPING == null) {
					JDB_ORM_SESSION_FACTORY_MAPPING = new HashMap<String, SessionFactory>(8);
				}
				if(JDB_ORM_SESSION_FACTORY_MAPPING.isEmpty() || !JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)){
					JDB_ORM_SESSION_FACTORY_MAPPING.put(sessionFactoryId, sessionFactory);
					return 2;
				}
			}
		}
		return 0;
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactory getSessionFactory() {
		if(JDB_ORM_SESSION_FACTORY_MAPPING == null || JDB_ORM_SESSION_FACTORY_MAPPING.isEmpty()) {// 没有动态添加SessionFactory时, 返回默认的SessionFactory
			if(DEFAULT_JDB_ORM_SESSION_FACTORY == null) {
				throw new NullPointerException("不存在任何 SessionFactory 实例");
			}
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
		}
		String sessionFactoryId = MultiSessionFactoryHandler.getSessionFactoryId();
		if(StringUtil.isEmpty(sessionFactoryId)) {
			throw new NullPointerException("在注册了多个SessionFactory后, 获取SessionFactory时, 必须要指定SessionFactory id");
		}
		
		if(JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			return JDB_ORM_SESSION_FACTORY_MAPPING.get(sessionFactoryId);
		}else if(DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
			return DEFAULT_JDB_ORM_SESSION_FACTORY;
		}
		throw new NotExistsSessionFactoryException(sessionFactoryId);
	}
	
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 * @return SessionFactory Map集合中是否还有 SessionFactory 实例
	 */
	static boolean destroySessionFactory(String sessionFactoryId) {
		if(JDB_ORM_SESSION_FACTORY_MAPPING != null && !JDB_ORM_SESSION_FACTORY_MAPPING.isEmpty() && JDB_ORM_SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			JDB_ORM_SESSION_FACTORY_MAPPING.remove(sessionFactoryId).destroy();
			return JDB_ORM_SESSION_FACTORY_MAPPING.size() > 0;
		}else {
			if(DEFAULT_JDB_ORM_SESSION_FACTORY.getId().equals(sessionFactoryId))
				throw new ProhibitDestroyDefaultSessionFactoryException(sessionFactoryId);
			throw new NotExistsSessionFactoryException(sessionFactoryId);
		}
	}
}
