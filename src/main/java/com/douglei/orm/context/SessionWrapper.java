package com.douglei.orm.context;

import com.douglei.orm.sessions.Session;

/**
 * 
 * @author DougLei
 */
class SessionWrapper {
	private Session session;
	private short count;
	private Throwable throwable;
	
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
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean ready() {
		return count == 1 || throwable != null;
	}
}
