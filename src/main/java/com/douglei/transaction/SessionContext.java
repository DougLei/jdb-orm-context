package com.douglei.transaction;

import java.util.Stack;

import com.douglei.SimpleSessionContext;
import com.douglei.core.dialect.TransactionIsolationLevel;
import com.douglei.sessions.Session;

/**
 * 通过@Transaction注解, 由系统自行控制session的的commit/rollback/close
 * @author DougLei
 */
public class SessionContext {
	private static final ThreadLocal<Stack<Session>> SESSIONS = new ThreadLocal<Stack<Session>>();
	
	public static Session getSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			throw new NullPointerException("不存在可用的seesion实例");
		}
		return sessions.peek();
	}
	
	static Session existsSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			return null;
		}
		return sessions.peek();
	}
	
	static Session openSession(boolean beginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			sessions = new Stack<Session>();
			SESSIONS.set(sessions);
		}
		Session session = SimpleSessionContext.getSession(beginTransaction, transactionIsolationLevel);
		sessions.push(session);
		return session;
	}
	
	// 获取并移除栈顶的session
	static Session popSession() {
		Stack<Session> sessions = SESSIONS.get();
		if(sessions == null || sessions.size() == 0) {
			throw new NullPointerException("不存在可用的seesion实例");
		}
		return sessions.pop();
	}
}
