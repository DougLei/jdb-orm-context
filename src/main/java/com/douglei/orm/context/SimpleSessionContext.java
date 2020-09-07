package com.douglei.orm.context;

import com.douglei.orm.core.dialect.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.sessions.Session;

/**
 * 对session的commit/rollback/close, 都由调用者自行控制
 * <b>每次调用都会产生一个新的session</b>
 * @author DougLei
 */
public class SimpleSessionContext {
	
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
		return SessionFactoryContext.get().openSession(beginTransaction, transactionIsolationLevel);
	}
}
