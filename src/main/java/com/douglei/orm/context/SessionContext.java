package com.douglei.orm.context;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.configuration.environment.datasource.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.orm.sessionfactory.sessions.Session;
import com.douglei.orm.sessionfactory.sessions.session.sql.SQLSession;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQuerySession;
import com.douglei.orm.sessionfactory.sessions.session.table.TableSession;
import com.douglei.orm.sessionfactory.sessions.sqlsession.SqlSession;

/**
 * 
 * @author DougLei
 */
public final class SessionContext {
	private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
	private static final ThreadLocal<Stack<SessionWrapper>> SESSION_WRAPPERS = new ThreadLocal<Stack<SessionWrapper>>();
	
	/**
	 * 获取当前的SessionFactory实例; 如果调用方通过该实例自己开启了Session, 则需要自己处理Session的提交/回滚/关闭
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return SessionFactoryContext.get();
	}
	
	/**
	 * 获取Session实例
	 * @return
	 */
	public static Session getSession() {
		SessionWrapper sessionWrapper = getSessionWrapper();
		Session session = sessionWrapper.getSession();
		logger.debug("get session is {}", sessionWrapper);
		return session;
	}
	
	/**
	 * 获取SqlSession实例
	 * @return
	 */
	public static SqlSession getSqlSession() {
		return getSession().getSqlSession();
	}
	
	/**
	 * 获取TableSession实例
	 * @return
	 */
	public static TableSession getTableSession() {
		return getSession().getTableSession();
	}
	
	/**
	 * 获取SQLSession实例
	 * @return
	 */
	public static SQLSession getSQLSession() {
		return getSession().getSQLSession();
	}
	
	/**
	 * 获取SQLQuerySession实例
	 * @return
	 */
	public static SQLQuerySession getSQLQuerySession() {
		return getSession().getSQLQuerySession();
	}
	
	/**
	 * 对当前事物进行commit, 会覆盖原本事物要执行的commit或rollback, 可以理解为手动(强制)提交
	 */
	public static void executeCommit() {
		getSessionWrapper().setTransactionExecuteMode(TransactionExecuteMoe.COMMIT);
	}
	
	/**
	 * 对当前事物进行rollback, 会覆盖原本事物要执行的commit或rollback, 可以理解为手动(强制)回滚
	 */
	public static void executeRollback() {
		getSessionWrapper().setTransactionExecuteMode(TransactionExecuteMoe.ROLLBACK);
	}
	
	// 获取SessionWrapper实例
	static SessionWrapper getSessionWrapper() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.isEmpty()) 
			throw new NullPointerException("不存在可用的seesion实例");
		return sessionWrappers.peek();
	}
	
	// 判断是否存在SessionWrapper
	static SessionWrapper existsSessionWrapper() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.isEmpty()) 
			return null;
		
		SessionWrapper sessionWrapper = sessionWrappers.peek();
		logger.debug("exists session is {}", sessionWrapper);
		return sessionWrapper;
	}
	
	// 开启Session
	static void openSession(boolean isBeginTransaction, TransactionIsolationLevel transactionIsolationLevel) {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		if(sessionWrappers == null || sessionWrappers.isEmpty()) {
			sessionWrappers = new Stack<SessionWrapper>();
			SESSION_WRAPPERS.set(sessionWrappers);
		}
		Session session = getSessionFactory().openSession(isBeginTransaction, transactionIsolationLevel);
		SessionWrapper sessionWrapper = new SessionWrapper(session, transactionIsolationLevel);
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
	static int numberOfSessionsLeft() {
		Stack<SessionWrapper> sessionWrappers = SESSION_WRAPPERS.get();
		return sessionWrappers == null ? 0 : sessionWrappers.size();
	}
}
