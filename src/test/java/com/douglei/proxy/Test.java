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
		
		Class<?>[] ints = ITest2.class.getInterfaces();
		System.out.println(ints.length);
		for (Class<?> class1 : ints) {
			System.out.println(class1.getName());
		}
		
		ITest test = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), ITest2.class.getInterfaces(), proxy);
		test.add();
	}
	private static Test t = new Test();
}
