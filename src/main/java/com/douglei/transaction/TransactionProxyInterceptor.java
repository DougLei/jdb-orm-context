package com.douglei.transaction;

import java.lang.reflect.Method;
import java.util.List;

import com.douglei.aop.ProxyInterceptor;

/**
 * 
 * @author DougLei
 */
public class TransactionProxyInterceptor extends ProxyInterceptor{
	// TODO 事务处理的核心

	public TransactionProxyInterceptor(Method method) {
		super(method);
	}
	public TransactionProxyInterceptor(List<Method> methods) {
		super(methods);
	}

	@Override
	protected boolean before(Object obj, Method method, Object[] args) {
		return super.before(obj, method, args);
	}

	@Override
	protected Object after(Object obj, Method method, Object[] args, Object result) {
		return super.after(obj, method, args, result);
	}

	@Override
	protected void exception(Object obj, Method method, Object[] args, Throwable t) {
		super.exception(obj, method, args, t);
	}
}
