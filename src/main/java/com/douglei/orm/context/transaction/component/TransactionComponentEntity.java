package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyMethod;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class TransactionComponentEntity {
	private String name;
	private Class<?> clazz;
	private List<ProxyMethod> methods;
	
	public TransactionComponentEntity(String name, Class<?> clazz) {
		if(StringUtil.isEmpty(name)) {
			name = clazz.getSimpleName();
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		}
		this.name = name;
		this.clazz = clazz;
	}
	
	public void addMethod(Method method) {
		if(methods == null)
			methods = new ArrayList<ProxyMethod>();
		methods.add(new ProxyMethod(method));
	}
	
	public String getName() {
		return name;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public List<ProxyMethod> getMethods() {
		return methods;
	}

	@Override
	public String toString() {
		return "TransactionComponentEntity [name="+name+", clazz="
				+ clazz.getName() + ", methods=" + methods + "]";
	}
}
