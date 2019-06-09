package com.douglei.transaction;

import java.lang.reflect.Method;
import java.util.List;

import com.douglei.aop.ProxyInterceptor;
import com.douglei.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class TransactionProxyInterceptor extends ProxyInterceptor{

	public TransactionProxyInterceptor(List<Method> methods) {
		super(methods);
	}

	@Override
	protected boolean before(Object obj, Method method, Object[] args) {
		Session session = null;
		Transaction transaction = method.getAnnotation(Transaction.class);
		switch(transaction.propagationBehavior()) {
			case REQUIRED:
				session = SessionContext.existsSession();
				if(session == null) {
					session = SessionContext.openSession(true, transaction.transactionIsolationLevel());
				}else if(!session.isBeginTransaction()) {
					session.beginTransaction();
					session.setTransactionIsolationLevel(transaction.transactionIsolationLevel());
				}
				break;
			case REQUIRED_NEW:
				SessionContext.openSession(true, transaction.transactionIsolationLevel());
				break;
			case SUPPORTS:
				session = SessionContext.existsSession();
				if(session == null) {
					session = SessionContext.openSession(false, transaction.transactionIsolationLevel());
				}
				break;
		}
		return true;
	}

	@Override
	protected Object after(Object obj, Method method, Object[] args, Object result) {
		SessionContext.popSession().commit();
		return result;
	}

	@Override
	protected void exception(Object obj, Method method, Object[] args, Throwable t) {
		SessionContext.popSession().rollback();
	}

	@Override
	protected void finally_(Object obj, Method method, Object[] args) {
		SessionContext.popSession().close();
	}
}
