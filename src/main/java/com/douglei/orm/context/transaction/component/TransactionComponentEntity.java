package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class TransactionComponentEntity {
	private Class<?> transactionComponentClass;
	private List<Method> transactionMethods;
	
	public TransactionComponentEntity(Class<?> transactionComponentClass, int methodSize) {
		this.transactionComponentClass = transactionComponentClass;
		transactionMethods = new ArrayList<Method>(methodSize);
	}

	public void addMethod(Method method) {
		transactionMethods.add(method);
	}
	
	public Class<?> getTransactionComponentClass() {
		return transactionComponentClass;
	}
	public List<Method> getTransactionMethods() {
		return transactionMethods;
	}

	@Override
	public String toString() {
		return "TransactionComponentProxyEntity [transactionComponentClass="
				+ transactionComponentClass.getName() + ", transactionMethods=" + transactionMethods + "]";
	}
}
