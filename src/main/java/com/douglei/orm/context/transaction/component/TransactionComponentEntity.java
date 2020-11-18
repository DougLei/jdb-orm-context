package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyMethod;

/**
 * 
 * @author DougLei
 */
public class TransactionComponentEntity {
	private Class<?> clazz;
	private List<ProxyMethod> methods;
	
	public TransactionComponentEntity(Class<?> clazz) {
		this(clazz, 10);
	}
	public TransactionComponentEntity(Class<?> clazz, int methodLength) {
		this.clazz = clazz;
		this.methods = new ArrayList<ProxyMethod>(methodLength);
	}

	public void addMethod(Method method) {
		methods.add(new ProxyMethod(method));
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	public List<ProxyMethod> getMethods() {
		return methods;
	}

	@Override
	public String toString() {
		return "TransactionComponentProxyEntity [clazz="
				+ clazz.getName() + ", methods=" + methods + "]";
	}
}
