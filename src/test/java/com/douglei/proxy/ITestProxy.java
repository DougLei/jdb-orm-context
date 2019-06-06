package com.douglei.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.douglei.transaction.Transaction;

public class ITestProxy implements InvocationHandler{
	
	private Object targetObject;
	public ITestProxy(Object targetObject) {
		this.targetObject = targetObject;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("begin");
		
		System.out.println(method.getAnnotation(Transaction.class));
		
		Object result = method.invoke(targetObject, args);
		System.out.println("end");
		return result;
	}
}
