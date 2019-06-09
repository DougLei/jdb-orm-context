package com.douglei.orm.context;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.core.dialect.TransactionIsolationLevel;
import com.douglei.orm.sessions.Session;

/**
 * 通过@Transaction注解, 由系统自行控制session的的commit/rollback/close
 * @author DougLei
 */
public class SessionContext {
	private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
	private static final ThreadLocal<Stack<Session>> SESSIONS = new ThreadLocal<Stack<Session>>();
	
	public static Session getSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			throw new NullPointerException("不存在可用的seesion实例");
		}
		Session session = sessions.peek();
		logger.debug("get session is {}", session);
		return session;
	}
	
	static Session existsSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			return null;
		}
		Session session = sessions.peek();
		logger.debug("exists session is {}", session);
		return session;
	}
	
	static Session openSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			sessions = new Stack<Session>();
			SESSIONS.set(sessions);
		}
		Session session = SessionFactoryContext.getSessionFactory().openSession(beginTransaction, transactionIsolationLevel);
		sessions.push(session);
		logger.debug("open session is {}", session);
		return session;
	}
	
	// 获取并移除栈顶的session
	static Session popSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			throw new NullPointerException("不存在可用的seesion实例");
		}
		Session session = sessions.pop();
		logger.debug("pop session is {}", session);
		return session;
	}
}
