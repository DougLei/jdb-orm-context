package com.douglei.orm.context;

/**
 * 当前线程要使用的SessionFactoryId的持有器
 * @author DougLei
 */
public class SessionFactoryIdHolder {
	private static final ThreadLocal<String> MULTI_SESSION_FACTORY = new ThreadLocal<String>();// 当前线程要使用的SessionFactory的Id
	
	/**
	 * 设置当前线程要使用的SessionFactory的Id
	 * @param sessionFactoryId
	 */
	public static void setId(String sessionFactoryId) {
		MULTI_SESSION_FACTORY.set(sessionFactoryId);
	}
	
	/**
	 * 获取当前线程要使用的SessionFactory的Id
	 * @return
	 */
	public static String getId() {
		return MULTI_SESSION_FACTORY.get();
	}
}
