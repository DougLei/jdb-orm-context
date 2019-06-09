package com.douglei.orm.context;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.aop.ProxyInterceptor;
import com.douglei.orm.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class TransactionProxyInterceptor extends ProxyInterceptor{
	private static final Logger logger = LoggerFactory.getLogger(TransactionProxyInterceptor.class);

	public TransactionProxyInterceptor(List<Method> methods) {
		super(methods);
	}

	@Override
	protected boolean before(Object obj, Method method, Object[] args) {
		Transaction transaction = method.getAnnotation(Transaction.class);
		switch(transaction.propagationBehavior()) {
			case REQUIRED:
				Session session_REQUIRED = SessionContext.existsSession();
				if(session_REQUIRED == null) {
					session_REQUIRED = SessionContext.openSession(true, transaction.transactionIsolationLevel());
				}else {
					if(!session_REQUIRED.isBeginTransaction()) {
						session_REQUIRED.beginTransaction();
					}
					session_REQUIRED.setTransactionIsolationLevel(transaction.transactionIsolationLevel());
				}
				break;
			case REQUIRED_NEW:
				SessionContext.openSession(true, transaction.transactionIsolationLevel());
				break;
			case SUPPORTS:
				Session session_SUPPORTS = SessionContext.existsSession();
				if(session_SUPPORTS == null) {
					session_SUPPORTS = SessionContext.openSession(false, transaction.transactionIsolationLevel());
				}else {
					session_SUPPORTS.setTransactionIsolationLevel(transaction.transactionIsolationLevel());
				}
				break;
		}
		return true;
	}

	@Override
	protected Object after(Object obj, Method method, Object[] args, Object result) {
		Session session = SessionContext.getSession();
		logger.debug("{} session do commit", session);
		session.commit();
		return result;
	}

	@Override
	protected void exception(Object obj, Method method, Object[] args, Throwable t) {
		Session session = SessionContext.getSession();
		logger.debug("{} session do rollback", session);
		session.rollback();
		t.printStackTrace();
	}

	@Override
	protected void finally_(Object obj, Method method, Object[] args) {
		Session session = SessionContext.popSession();
		logger.debug("{} session do close", session);
		session.close();
	}
}
