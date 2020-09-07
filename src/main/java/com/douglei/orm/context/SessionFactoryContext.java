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
	private static SessionFactory UNIQUE; // 当MAPPING中只有一个SessionFactoryWrapper时, 该属性引用唯一的SessionFactoryWrapper; 当MAPPING中有多个或没有SessionFactoryWrapper时, 该属性都为空
	private static Map<String, SessionFactory> MAPPING = new HashMap<String, SessionFactory>(4);
	
	// 设置UNIQUE的值
	private static void setUNIQUE() {
		if(MAPPING.size() == 1) {
			UNIQUE = MAPPING.values().iterator().next();
		}else {
			UNIQUE = null;
		}
	}
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 * @return 
	 * @throws IdDuplicateException 
	 */
	static RegistrationResult register(SessionFactory sessionFactory) throws IdDuplicateException {
		SessionFactory sf = null;
		if(!MAPPING.isEmpty())
			sf = MAPPING.get(sessionFactory.getId());
		
		if(sf == null) {
			MAPPING.put(sessionFactory.getId(), sessionFactory);
			setUNIQUE();
			return RegistrationResult.SUCCESS;
		}else {
			if(sf != sessionFactory)
				throw new IdDuplicateException("已经存在id为"+sessionFactory.getId()+"的SessionFactory实例");
			setUNIQUE();
			return RegistrationResult.ALREADY_EXISTS;
		}
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactory get() {
		if(MAPPING.isEmpty())
			throw new NullPointerException("不存在任何SessionFactory实例");
		
		String id = SessionFactoryIdHolder.getId();
		if(StringUtil.isEmpty(id)) {
			if(UNIQUE == null) // 说明有多个数据源
				throw new NullPointerException("请指定要获取SessionFactory的实例id值");
			return UNIQUE;
		}
		
		if(UNIQUE != null) {
			if(id.equals(UNIQUE.getId()))
				return UNIQUE;
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, get失败");
		}
		
		SessionFactory sf = MAPPING.get(id);
		if(sf == null)
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, get失败");
		return sf;
	}
	
	/**
	 * 移除SessionFactory
	 * @param id
	 * @param destroy 是否在移除的同时进行销毁
	 * @return 
	 */
	static SessionFactory remove(String id, boolean destroy) {
		if(MAPPING.isEmpty())
			throw new NullPointerException("不存在任何SessionFactory实例");
		
		SessionFactory sf = MAPPING.remove(id);
		if(sf == null)
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, "+(destroy?"destroy":"remove")+"失败");
		
		if(destroy)
			sf.destroy();
		setUNIQUE();
		return sf;
	}
}
