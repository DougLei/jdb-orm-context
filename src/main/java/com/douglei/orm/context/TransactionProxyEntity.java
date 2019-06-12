package com.douglei.orm.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class TransactionProxyEntity {
	private Class<?> transactionClass;
	private List<Method> transactionAnnotationMethods;
	
	public TransactionProxyEntity(Class<?> transactionClass, int methodSize) {
		this.transactionClass = transactionClass;
		transactionAnnotationMethods = new ArrayList<Method>(methodSize);
	}

	public void addMethod(Method method) {
		transactionAnnotationMethods.add(method);
	}
	
	public Class<?> getTransactionClass() {
		return transactionClass;
	}
	public List<Method> getTransactionAnnotationMethods() {
		return transactionAnnotationMethods;
	}
}
