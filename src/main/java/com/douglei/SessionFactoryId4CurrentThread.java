package com.douglei;

/**
 * 当前线程要使用的SessionFactoryId
 * @author DougLei
 */
public class SessionFactoryId4CurrentThread {
	private static final ThreadLocal<String> SESSION_FACTORY_ID_4_CURRENT_THREAD = new ThreadLocal<String>();// 当前线程要使用的SessionFactory的Id
	
	/**
	 * 设置当前线程要使用的SessionFactory的Id
	 * @param sessionFactoryId
	 */
	public static void setSessionFactoryId4CurrentThread(String sessionFactoryId) {
		SESSION_FACTORY_ID_4_CURRENT_THREAD.set(sessionFactoryId);
	}
	
	/**
	 * 获取
	 * @return
	 */
	public static String getSessionFactoryId4CurrentThread() {
		return SESSION_FACTORY_ID_4_CURRENT_THREAD.get();
	}
}
