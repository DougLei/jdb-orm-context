package com.douglei;

import com.douglei.core.dialect.TransactionIsolationLevel;
import com.douglei.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class TransactionSessionContext {
	private static final ThreadLocal<Session> SESSION = new ThreadLocal<Session>();
	
	/**
	 * <pre>
	 * 	开启Session实例
	 * 	默认开启事物
	 * </pre>
	 * @return
	 */
	public static Session getSession() {
		return getSession(true);
	}
	
	/**
	 * <pre>
	 * 	开启Session实例
	 * </pre>
	 * @param beginTransaction 是否开启事物
	 * @return
	 */
	public static Session getSession(boolean beginTransaction) {
		return getSession(beginTransaction, null);
	}
	
	/**
	 * <pre>
	 * 	开启Session实例
	 * </pre>
	 * @param beginTransaction 是否开启事物
	 * @param transactionIsolationLevel 事物隔离级别, 如果传入null, 则使用jdbc默认的隔离级别
	 * @return
	 */
	public static Session getSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Session session = SESSION.get();
		if(session == null) {
			session = SessionFactoryContext.getSessionFactory().openSession(beginTransaction, transactionIsolationLevel);
			SESSION.set(session);
		}
		return session;
	}
}
