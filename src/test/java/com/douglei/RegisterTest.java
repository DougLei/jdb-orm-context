package com.douglei;

/**
 * 
 * @author DougLei
 */
public class RegisterTest {
	public static void main(String[] args) {
		SessionFactoryRegister register = SessionFactoryRegister.singleInstance();
		register.registerDefaultSessionFactory();
		
		
		
	}
}
