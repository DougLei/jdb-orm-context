package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.environment.datasource.TransactionIsolationLevel;
import com.douglei.orm.sessionfactory.sessions.Session;
import com.douglei.tools.utils.ExceptionUtil;

/**
 * 
 * @author DougLei
 */
class SessionWrapper {
	private static final Logger logger = LoggerFactory.getLogger(SessionWrapper.class);
	
	private Session session;
	private byte count; // 被多少个方法使用
	private List<TransactionIsolationLevel> transactionIsolationLevels;
	private List<Throwable> throwables;
	
	SessionWrapper(Session session, TransactionIsolationLevel transactionIsolationLevel) {
		this.session = session;
		this.count = 1;
		this.transactionIsolationLevels = new ArrayList<TransactionIsolationLevel>();
		this.transactionIsolationLevels.add(transactionIsolationLevel);
	}
	
	public Session getSession() {
		return session;
	}
	public SessionWrapper increment(TransactionIsolationLevel transactionIsolationLevel) {
		count++;
		transactionIsolationLevels.add(transactionIsolationLevel);
		session.setTransactionIsolationLevel(transactionIsolationLevel);
		return this;
	}
	public SessionWrapper decrement() {
		count--;
		transactionIsolationLevels.remove(count);
		session.setTransactionIsolationLevel(transactionIsolationLevels.get(count-1));
		return this;
	}
	public void pushThrowable(Throwable throwable) {
		if(this.throwables == null) {
			this.throwables = new ArrayList<Throwable>();
		}else if(this.throwables.contains(throwable)) {
			return;
		}
		this.throwables.add(throwable);
	}

	public boolean ready() {
		return count == 1;
	}
	public boolean readyCommit() throws Throwable{
		if(throwables != null) {
			throw throwables.get(throwables.size()-1);
		}
		return count == 1;
	}

	public void throwThrowable() throws Throwable {
		for(int i=throwables.size()-1;i>=0;i--) {
			logger.error(ExceptionUtil.getExceptionDetailMessage(throwables.get(i)));
		}
		throw throwables.get(throwables.size()-1);
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
