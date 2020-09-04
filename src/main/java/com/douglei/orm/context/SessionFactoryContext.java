package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.context.exception.DuplicateRegisterSessionFactoryException;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.utils.StringUtil;

/**
 * jdb-orm SessionFactory 上下文
 * @author DougLei
 */
class SessionFactoryContext {
	private static SessionFactoryWrapper UNIQUE; // 当MAPPING中只有一个SessionFactoryWrapper时, 该属性引用唯一的SessionFactoryWrapper; 当MAPPING中有多个或没有SessionFactoryWrapper时, 该属性都为空
	private static Map<String, SessionFactoryWrapper> MAPPING = new HashMap<String, SessionFactoryWrapper>(4);
	
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
	 * @param feature
	 * @throws DuplicateRegisterSessionFactoryException 
	 */
	static void register(SessionFactory sessionFactory, SessionFactoryRegistrationFeature feature) throws DuplicateRegisterSessionFactoryException {
		SessionFactoryWrapper sfw = MAPPING.get(sessionFactory.getId());
		if(sfw == null) {
			MAPPING.put(sessionFactory.getId(), new SessionFactoryWrapper(sessionFactory, feature));
		}else {
			if(sfw.getSessionFactory() == sessionFactory)
				System.out.println(1);
			throw new DuplicateRegisterSessionFactoryException(sessionFactory.getId());
		}
		setUNIQUE();
	}
	
	/**
	 * 获取SessionFactory
	 * @return
	 */
	static SessionFactoryWrapper get() {
		if(MAPPING.isEmpty())
			throw new NullPointerException("不存在任何SessionFactory实例");
		
		String id = SessionFactoryIdHolder.getId();
		if(StringUtil.isEmpty(id)) {
			if(UNIQUE == null) // 说明有多个数据源
				throw new NullPointerException("请指定要获取SessionFactory的实例id值");
			return UNIQUE;
		}
		
		if(UNIQUE != null) {
			if(id.equals(UNIQUE.getSessionFactory().getId()))
				return UNIQUE;
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, get失败");
		}
		
		SessionFactoryWrapper sfw = MAPPING.get(id);
		if(sfw == null)
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, get失败");
		return sfw;
	}
	
	/**
	 * 移除SessionFactory
	 * @param id
	 * @return 被移除的SessionFactoryWrapper
	 */
	static SessionFactoryWrapper remove(String id) {
		if(MAPPING.isEmpty())
			throw new NullPointerException("不存在任何SessionFactory实例");
		
		SessionFactoryWrapper sfw = MAPPING.remove(id);
		if(sfw == null)
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, remove失败");
		
		if(!sfw.getFeature().isAllowRemove())
			throw new IllegalArgumentException("id为"+id+"的SessionFactory实例不允许被remove");
		
		setUNIQUE();
		return sfw;
	}
	
	/**
	 * 销毁SessionFactory
	 * @param id
	 */
	static void destroy(String id) {
		if(MAPPING.isEmpty())
			throw new NullPointerException("不存在任何SessionFactory实例");
		
		SessionFactoryWrapper sfw = MAPPING.remove(id);
		if(sfw == null)
			throw new NullPointerException("不存在id为"+id+"的SessionFactory实例, destroy失败");
		
		if(!sfw.getFeature().isAllowDestroy())
			throw new IllegalArgumentException("id为"+id+"的SessionFactory实例不允许被destroy");
		
		sfw.getSessionFactory().destroy();
		setUNIQUE();
	}
}
