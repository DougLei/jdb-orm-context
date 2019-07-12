package com.douglei.orm.context.transaction.component;

import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyMethod;

/**
 * 
 * @author DougLei
 */
public class TransactionComponentEntity {
	private Class<?> transactionComponentClass;
	private List<ProxyMethod> transactionMethods;
	
	public TransactionComponentEntity(Class<?> transactionComponentClass, int methodSize) {
		this.transactionComponentClass = transactionComponentClass;
		transactionMethods = new ArrayList<ProxyMethod>(methodSize);
	}

	public void addMethod(ProxyMethod method) {
		transactionMethods.add(method);
	}
	
	public Class<?> getTransactionComponentClass() {
		return transactionComponentClass;
	}
	public List<ProxyMethod> getTransactionMethods() {
		return transactionMethods;
	}

	@Override
	public String toString() {
		return "TransactionComponentProxyEntity [transactionComponentClass="
				+ transactionComponentClass.getName() + ", transactionMethods=" + transactionMethods + "]";
	}
}
