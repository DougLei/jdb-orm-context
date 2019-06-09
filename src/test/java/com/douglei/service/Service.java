package com.douglei.service;

import com.douglei.PropagationBehavior;
import com.douglei.SessionContext;
import com.douglei.Transaction;
import com.douglei.aop.ProxyBeanContext;

public class Service {

	@Transaction
	public void add() {
		System.out.println("Service.add()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.add()");
		
		ProxyBeanContext.getProxy(Service.class).delete();
	}

	@Transaction(propagationBehavior=PropagationBehavior.REQUIRED_NEW)
	public void delete() {
		System.out.println("Service.delete()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.delete()");
	}
}
