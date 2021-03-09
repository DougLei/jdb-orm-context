package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.StringUtil;

/**
 * jdb-orm SessionFactory 上下文
 * @author DougLei
 */
class SessionFactoryContext {
	private static SessionFactory unique; // 当MAPPING中只有一个SessionFactoryWrapper时, 该属性引用唯一的SessionFactoryWrapper; 当MAPPING中有多个或没有SessionFactoryWrapper时, 该属性都为空
	private static Map<String, SessionFactory> sessionFactoryMapping = new HashMap<String, SessionFactory>(4);
	
	// 设置UNIQUE的值
	private static void setUNIQUE() {
		if(sessionFactoryMapping.size() == 1) {
			unique = sessionFactoryMapping.values().iterator().next();
		}else {
			unique = null;
		}
	}
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 * @return 
	 */
	static RegistrationResult register(SessionFactory sessionFactory) {
		SessionFactory sf = null;
		if(!sessionFactoryMapping.isEmpty())
			sf = sessionFactoryMapping.get(sessionFactory.getId());
		
		if(sf == null) {
			sessionFactoryMapping.put(sessionFactory.getId(), sessionFactory);
			setUNIQUE();
			return RegistrationResult.SUCCESS;
		}
		
		if(sf != sessionFactory)
			throw new OrmContextException("已经存在id为"+sessionFactory.getId()+"的实例");
		setUNIQUE();
		return RegistrationResult.ALREADY_EXISTS;
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactory get() {
		if(sessionFactoryMapping.isEmpty())
			throw new OrmContextException("不存在任何SessionFactory实例");
		
		String id = SessionFactoryIdHolder.getId();
		if(StringUtil.isEmpty(id)) {
			if(unique == null) // 说明有多个数据源
				throw new OrmContextException("请指定要获取SessionFactory的实例id值");
			return unique;
		}
		
		if(unique != null) {
			if(id.equals(unique.getId()))
				return unique;
			throw new OrmContextException("不存在id为"+id+"的SessionFactory实例, get失败");
		}
		
		SessionFactory sf = sessionFactoryMapping.get(id);
		if(sf == null)
			throw new OrmContextException("不存在id为"+id+"的SessionFactory实例, get失败");
		return sf;
	}
	
	/**
	 * 移除SessionFactory
	 * @param id
	 * @param destroy 是否在移除的同时进行销毁
	 * @return 
	 */
	static SessionFactory remove(String id, boolean destroy) {
		if(sessionFactoryMapping.isEmpty())
			throw new OrmContextException("不存在任何SessionFactory实例");
		
		SessionFactory sf = sessionFactoryMapping.remove(id);
		if(sf == null)
			throw new OrmContextException("不存在id为"+id+"的SessionFactory实例, "+(destroy?"destroy":"remove")+"失败");
		
		if(destroy)
			sf.destroy();
		setUNIQUE();
		return sf;
	}
}
