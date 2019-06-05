package com.douglei;

import com.douglei.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class SessionContext {
	private static final ThreadLocal<Session> SESSION = new ThreadLocal<Session>();
	
	/**
	 * 获取session
	 * @return
	 */
	public static Session getSession() {
		Session session = SESSION.get();
		if(session == null) {
			session = SessionFactoryContext.getSessionFactory().openSession();
			SESSION.set(session);
		}
		return session;
	}
}
