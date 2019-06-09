package com.douglei.orm.context.service;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

public class Service {

	@Transaction
	public void add() {
		System.out.println("Service.add()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.add()");
		
		ProxyBeanContext.getProxy(Service.class).delete();
	}

//	@Transaction(propagationBehavior=PropagationBehavior.REQUIRED_NEW)
	@Transaction
	public void delete() {
		System.out.println("Service.delete()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.delete()");
	}
}
