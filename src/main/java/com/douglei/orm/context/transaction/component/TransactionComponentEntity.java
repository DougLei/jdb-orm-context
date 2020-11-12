package com.douglei.orm.context.transaction.component;

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
	
	public TransactionComponentEntity(Class<?> clazz, int methodSize) {
		this.clazz = clazz;
		this.methods = new ArrayList<ProxyMethod>(methodSize);
	}

	public void addMethod(ProxyMethod method) {
		methods.add(method);
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
