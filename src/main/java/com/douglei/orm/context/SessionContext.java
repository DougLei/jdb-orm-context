package com.douglei.orm.context;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.core.dialect.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.orm.sessionfactory.sessions.Session;
import com.douglei.orm.sessionfactory.sessions.session.sql.SQLSession;
import com.douglei.orm.sessionfactory.sessions.session.table.TableSession;
import com.douglei.orm.sessionfactory.sessions.sqlsession.SqlSession;

/**
 * 通过@Transaction注解, 由系统自行控制session的的commit/rollback/close
 * @author DougLei
 */
public final class SessionContext {
	private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
	private static final ThreadLocal<Stack<SessionWrapper>> SESSION_WRAPPERS = new ThreadLocal<Stack<SessionWrapper>>();
	
	public static SessionFactory getSessionFactory() {
		return SessionFactoryContext.get().getSessionFactory();
	}
	
	public static Session getSession() {
		SessionWrapper sessionWrapper = getSessionWrapper();
		Session session = sessionWrapper.getSession();
		logger.debug("get session is {}", sessionWrapper);
		return session;
	}
	public static SqlSession getSqlSession() {
		return getSession().getSqlSession();
	}
	public static TableSession getTableSession() {
		return getSession().getTableSession();
	}
	public static SQLSession getSQLSession() {
		return getSession().getSQLSession();
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
		Session session = getSessionFactory().openSession(beginTransaction, transactionIsolationLevel);
		SessionWrapper sessionWrapper = new SessionWrapper(session);
		sessionWrappers.push(sessionWrapper);
		logger.debug("open session is {}", sessionWrapper);
	}
	
	// 获取并移除栈顶的session, 调用该方法前, 请务必先调用getSessionWrapper().ready()方法, 判断是否满足了出栈的条件
	static SessionWrapper popSessionWrapper() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		SessionWrapper sessionWrapper = sessionWrappers.pop();
		logger.debug("pop session is {}", sessionWrapper);
		
		if(sessionWrappers.isEmpty()) {
			SESSION_WRAPPERS.remove();
			logger.debug("session stack is empty, remove session stack from ThreadLocal");
		}
		return sessionWrapper;
	}
	
	/**
	 * 剩下session的数量
	 * @return
	 */
	public static int numberOfSessionsLeft() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		return sessionWrappers == null ? 0 : sessionWrappers.size();
	}
}
