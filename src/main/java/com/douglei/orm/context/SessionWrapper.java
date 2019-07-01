package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.sessions.Session;

/**
 * 
 * @author DougLei
 */
class SessionWrapper {
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
	public void setThrowable(Throwable throwable) {
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
			throwables.get(i).printStackTrace();
		}
	}

	@Override
	public String toString() {
		return session.toString();
	}
}
