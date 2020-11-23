package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.configuration.environment.datasource.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.sessions.Session;
import com.douglei.tools.utils.ExceptionUtil;

/**
 * 
 * @author DougLei
 */
class SessionWrapper {
	private static final Logger logger = LoggerFactory.getLogger(SessionWrapper.class);
	static final byte COMMIT = 1; // 事物执行模式为commit: 1
	static final byte ROLLBACK = 2; // 事物执行模式为rollback: 2
	
	private Session session;
	private byte count; // 被多少个方法使用
	private List<TransactionIsolationLevel> transactionIsolationLevels;
	private List<Throwable> throwables;
	private byte transactionExecuteMode; // 指定的事物执行模式, 在最后commit/rollback时, 如果有指定, 则根据指定的来执行, 否则按照程序逻辑执行
	
	SessionWrapper(Session session, TransactionIsolationLevel transactionIsolationLevel) {
		this.session = session;
		this.count = 1;
		this.transactionIsolationLevels = new ArrayList<TransactionIsolationLevel>();
		this.transactionIsolationLevels.add(transactionIsolationLevel);
	}
	
	/**
	 * 获取Session实例
	 * @return
	 */
	public Session getSession() {
		return session;
	}
	
	/**
	 * 当前Session的使用次数加1, 且设置新的事物隔离级别
	 * @param transactionIsolationLevel
	 * @return
	 */
	public SessionWrapper increment(TransactionIsolationLevel transactionIsolationLevel) {
		count++;
		transactionIsolationLevels.add(transactionIsolationLevel);
		session.setTransactionIsolationLevel(transactionIsolationLevel);
		return this;
	}
	
	/**
	 * 当前Session的使用次数减1, 且设置上一个事物隔离级别
	 * @param transactionIsolationLevel
	 * @return
	 */
	public SessionWrapper decrement() {
		count--;
		transactionIsolationLevels.remove(count);
		session.setTransactionIsolationLevel(transactionIsolationLevels.get(count-1));
		return this;
	}

	/**
	 * 设置事物执行的模式; 模式值为: {@link SessionWrapper.COMMIT}, {@link SessionWrapper.ROLLBACK}
	 * @param transactionExecuteMode
	 */
	public void setTransactionExecuteMode(byte transactionExecuteMode) {
		this.transactionExecuteMode = transactionExecuteMode;
	}
	
	/**
	 * 获取事物执行的模式; 模式值为: {@link SessionWrapper.COMMIT}, {@link SessionWrapper.ROLLBACK}
	 * @return
	 */
	public byte getTransactionExecuteMode() {
		return transactionExecuteMode;
	}
	
	public boolean ready() {
		return count == 1;
	}
	public boolean readyCommit() throws Throwable{
		if(throwables == null) 
			return count == 1;
		throw throwables.get(0);
	}

	public void addThrowable(Throwable throwable) {
		if(this.throwables == null) {
			this.throwables = new ArrayList<Throwable>();
		}else if(this.throwables.get(0) == throwable) {
			return;
		}
		this.throwables.add(throwable);
	}
	public void throwThrowables() throws Throwable {
		for(int i=0;i<throwables.size()-1;i++) 
			throwables.get(i).addSuppressed(throwables.get(i+1));
		Throwable throwable = throwables.get(0);
		logger.error(ExceptionUtil.getExceptionDetailMessage(throwable));
		throw throwable;
	}
	
	public void close() {
		if(throwables != null) {
			throwables.clear();
			throwables = null;
		}
		session.close();
	}
	
	@Override
	public String toString() {
		return session.toString();
	}
}
