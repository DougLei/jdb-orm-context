package com.douglei.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Test implements ITest {

	@Override
	public void add() {
		System.out.println("add method");
	}
	
	public static void main(String[] args) {
		InvocationHandler proxy = new ITestProxy(t);
		
		ITest test = (ITest) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), proxy);
		test.add();
		test.add();
	}
	private static Test t = new Test();
}
