package com.douglei.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.douglei.transaction.Transaction;

public class Test implements ITest {

	@Transaction
	@Override
	public void add() {
		System.out.println("add method");
	}
	
	public static void main(String[] args) {
		InvocationHandler proxy = new ITestProxy(t);
		
		
		ITest test = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), ITest2.class.getInterfaces(), proxy);
		test.add();
		
		System.out.println(new Object().getClass().getEnclosingMethod().getName());
		
	}
	private static Test t = new Test();
}
