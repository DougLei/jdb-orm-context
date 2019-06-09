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
				SessionWrapper sessionWrapper_REQUIRED = SessionContext.existsSessionWrapper();
				if(sessionWrapper_REQUIRED == null) {
					SessionContext.openSession(true, transaction.transactionIsolationLevel());
				}else {
					Session session_REQUIRED = sessionWrapper_REQUIRED.increment().getSession();
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
				SessionWrapper sessionWrapper_SUPPORTS = SessionContext.existsSessionWrapper();
				if(sessionWrapper_SUPPORTS == null) {
					SessionContext.openSession(false, transaction.transactionIsolationLevel());
				}else {
					sessionWrapper_SUPPORTS.increment().getSession().setTransactionIsolationLevel(transaction.transactionIsolationLevel());
				}
				break;
		}
		return true;
	}

	@Override
	protected Object after(Object obj, Method method, Object[] args, Object result) throws Throwable {
		SessionWrapper sessionWrapper = SessionContext.getSessionWrapper();
		if(sessionWrapper.readyCommit()) {
			logger.debug("{} session do commit", sessionWrapper);
			sessionWrapper.getSession().commit();
		}
		return result;
	}

	@Override
	protected void exception(Object obj, Method method, Object[] args, Throwable t) {
		SessionWrapper sessionWrapper = SessionContext.getSessionWrapper();
		sessionWrapper.setThrowable(t);
		if(sessionWrapper.ready()) {
			logger.debug("{} session do rollback", sessionWrapper);
			sessionWrapper.getSession().rollback();
			sessionWrapper.printStackTraces();
		}
	}

	@Override
	protected void finally_(Object obj, Method method, Object[] args) {
		SessionWrapper sessionWrapper = SessionContext.getSessionWrapper();
		if(sessionWrapper.ready()) {
			Session session = SessionContext.popSession();
			logger.debug("{} session do close", sessionWrapper);
			session.close();
		}else {
			logger.debug("{} session not ready to close", sessionWrapper);
			sessionWrapper.decrement();
		}
	}
}
