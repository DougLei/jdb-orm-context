package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.orm.sessions.Session;
import com.douglei.tools.utils.ExceptionUtil;

/**
 * 
 * @author DougLei
 */
class SessionWrapper {
	private static final Logger logger = LoggerFactory.getLogger(SessionWrapper.class);
	
	private Session session;
	private short count;
	private List<Throwable> throwables;
	
	SessionWrapper(Session session) {
		this.session = session;
		this.count = 1;
	}
	
	public Session getSession() {
		return session;
	}
	public SessionWrapper increment() {
		count++;
		return this;
	}
	public SessionWrapper decrement() {
		count--;
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

	public void printStackTraces() {
		for(int i=throwables.size()-1;i>=0;i--) {
			logger.error(ExceptionUtil.getExceptionDetailMessage(throwables.get(i)));
		}
	}

	@Override
	public String toString() {
		return session.toString();
	}
}
