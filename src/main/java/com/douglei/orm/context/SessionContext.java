package com.douglei.orm.context;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.context.exception.UnsupportUseSessionContextException;
import com.douglei.orm.core.dialect.TransactionIsolationLevel;
import com.douglei.orm.sessions.Session;

/**
 * 通过@Transaction注解, 由系统自行控制session的的commit/rollback/close
 * @author DougLei
 */
public class SessionContext {
	private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
	private static final ThreadLocal<Stack<SessionWrapper>> SESSION_WRAPPERS = new ThreadLocal<Stack<SessionWrapper>>();
	private static boolean unUseTransactionAnnoation;// TODO 是否没有使用Transaction注解, 如果没有使用, 则不能使用该类获取session
	
	public static Session getSession() {
		if(unUseTransactionAnnoation) {
			throw new UnsupportUseSessionContextException();
		}
		SessionWrapper sessionWrapper = getSessionWrapper();
		Session session = sessionWrapper.getSession();
		logger.debug("get session is {}", sessionWrapper);
		return session;
	}
	
	static SessionWrapper getSessionWrapper() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.size() == 0) {
			throw new NullPointerException("不存在可用的seesion实例");
		}
		return sessionWrappers.peek();
	}
	
	static SessionWrapper existsSessionWrapper() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.size() == 0) {
			return null;
		}
		SessionWrapper sessionWrapper = sessionWrappers.peek();
		logger.debug("exists session is {}", sessionWrapper);
		return sessionWrapper;
	}
	
	static void openSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.size() == 0) {
			sessionWrappers = new Stack<SessionWrapper>();
			SESSION_WRAPPERS.set(sessionWrappers);
		}
		Session session = SessionFactoryContext.getSessionFactory().openSession(beginTransaction, transactionIsolationLevel);
		SessionWrapper sessionWrapper = new SessionWrapper(session);
		sessionWrappers.push(sessionWrapper);
		logger.debug("open session is {}", sessionWrapper);
	}
	
	// 获取并移除栈顶的session, 调用该方法前, 请务必先调用getSessionWrapper()方法, 判断是否满足了出栈的条件
	static Session popSession() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		Session session = sessionWrappers.pop().getSession();
		logger.debug("pop session is {}", session);
		return session;
	}
}
