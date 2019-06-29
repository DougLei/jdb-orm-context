package com.douglei.orm.context.service;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

public class Service {

	@Transaction
	public void add() {
		System.out.println("Service.add()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.add()");
		
		ProxyBeanContext.getProxy(Service.class).delete();
		ProxyBeanContext.getProxy(Service.class).delete2();
		ProxyBeanContext.getProxy(Service.class).delete3();
	}

//	@Transaction(propagationBehavior=PropagationBehavior.REQUIRED_NEW)
	@Transaction
	public void delete() {
		System.out.println("Service.delete()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.delete()");
		throw new NullPointerException("Service.delete1()");
	}
	
	@Transaction
	public void delete2() {
		System.out.println("Service.delete2()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.delete2()");
		throw new NullPointerException("Service.delete2()");
	}
	
	@Transaction(propagationBehavior=PropagationBehavior.REQUIRED_NEW)
	public void delete3() {
		System.out.println("Service.delete3()");
		System.out.println(SessionContext.getSession());
		System.out.println("Service.delete3()");
	}
}
