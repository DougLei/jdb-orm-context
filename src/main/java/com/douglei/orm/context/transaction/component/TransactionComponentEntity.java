package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class TransactionComponentEntity {
	private Class<?> transactionComponentProxyBeanClass;
	private List<Method> transactionMethods;
	
	public TransactionComponentEntity(Class<?> transactionComponentProxyBeanClass, int methodSize) {
		this.transactionComponentProxyBeanClass = transactionComponentProxyBeanClass;
		transactionMethods = new ArrayList<Method>(methodSize);
	}

	public void addMethod(Method method) {
		transactionMethods.add(method);
	}
	
	public Class<?> getTransactionComponentProxyBeanClass() {
		return transactionComponentProxyBeanClass;
	}
	public List<Method> getTransactionMethods() {
		return transactionMethods;
	}

	@Override
	public String toString() {
		return "TransactionComponentProxyEntity [transactionComponentProxyBeanClass="
				+ transactionComponentProxyBeanClass.getName() + ", transactionMethods=" + transactionMethods + "]";
	}
}
