package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.utils.StringUtil;

/**
 * jdb-orm SessionFactory 上下文
 * @author DougLei
 */
class SessionFactoryContext {
	private static SessionFactory DEFAULT_SESSION_FACTORY;// 默认的jdb-orm SessionFactory对象
	private static Map<String, SessionFactory> SESSION_FACTORY_MAPPING;// jdb-orm SessionFactory映射
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 * @return 0 传入的SessionFactory已经注册过, 1 将传入的SessionFactory注册为默认数据源, 2 将传入的SessionFactory注册到数据源Map集合中
	 */
	static byte registerSessionFactory(SessionFactory sessionFactory) {
		if(DEFAULT_SESSION_FACTORY == null) {
			DEFAULT_SESSION_FACTORY = sessionFactory;
			return 1;
		}else {
			String sessionFactoryId = sessionFactory.getId();
			if(!DEFAULT_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
				if(SESSION_FACTORY_MAPPING == null) {
					SESSION_FACTORY_MAPPING = new HashMap<String, SessionFactory>(8);
				}
				if(SESSION_FACTORY_MAPPING.isEmpty() || !SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)){
					SESSION_FACTORY_MAPPING.put(sessionFactoryId, sessionFactory);
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
		if(SESSION_FACTORY_MAPPING == null || SESSION_FACTORY_MAPPING.isEmpty()) {// 没有动态添加SessionFactory时, 返回默认的SessionFactory
			if(DEFAULT_SESSION_FACTORY == null) {
				throw new NullPointerException("不存在任何 SessionFactory 实例");
			}
			return DEFAULT_SESSION_FACTORY;
		}
		
		String sessionFactoryId = SessionFactoryIdHolder.getId();
		if(StringUtil.isEmpty(sessionFactoryId) || DEFAULT_SESSION_FACTORY.getId().equals(sessionFactoryId)) {
			return DEFAULT_SESSION_FACTORY; // 在注册了多个SessionFactory后, 获取SessionFactory时, 如果没有指定SessionFactory的id, 则返回默认数据源
		}
		
		if(SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) 
			return SESSION_FACTORY_MAPPING.get(sessionFactoryId);
		throw new NullPointerException("不存在id为"+sessionFactoryId+"的SessionFactory实例");
	}
	
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 * @return SessionFactory Map集合中是否还有 SessionFactory 实例
	 */
	static boolean destroySessionFactory(String sessionFactoryId) {
		if(SESSION_FACTORY_MAPPING != null && !SESSION_FACTORY_MAPPING.isEmpty() && SESSION_FACTORY_MAPPING.containsKey(sessionFactoryId)) {
			SESSION_FACTORY_MAPPING.remove(sessionFactoryId).destroy();
			return SESSION_FACTORY_MAPPING.size() > 0;
		}
		if(DEFAULT_SESSION_FACTORY.getId().equals(sessionFactoryId))
			throw new IllegalArgumentException("禁止销毁默认数据源");
		throw new NullPointerException("不存在id为"+sessionFactoryId+"的SessionFactory实例");
	}
}
