package com.douglei;

public class Test {
	public static void main(String[] args) {
		Test test = new Test();
		test.add();
	}
	
	public void add() {
		System.out.println("Test.add()");
		
		StackTraceElement[] stack = new Exception().getStackTrace();
		System.out.println(stack[0].getMethodName());
	}
}
