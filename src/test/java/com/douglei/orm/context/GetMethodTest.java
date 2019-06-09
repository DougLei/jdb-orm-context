package com.douglei.orm.context;

import java.lang.reflect.Method;

import com.douglei.orm.context.Transaction;

public class GetMethodTest {
	public static void main(String[] args) throws Exception {
		Class<?> clz = Class.forName("com.douglei.GetMethod");
		
		System.out.println("***********************************************************************");
		System.out.println("declared method");
		System.out.println("***********************************************************************");
		Method[] ms = clz.getDeclaredMethods();
		System.out.println(ms.length);
		for (Method method : ms) {
			if(method.getAnnotation(Transaction.class) != null) {
				System.out.println(method.getName());
			}
		}
		
//		System.out.println("\n");
//		System.out.println("***********************************************************************");
//		System.out.println("method");
//		System.out.println("***********************************************************************");
//		ms = clz.getMethods();
//		for (Method method : ms) {
//			System.out.println(method.getName());
//		}
	}
}
