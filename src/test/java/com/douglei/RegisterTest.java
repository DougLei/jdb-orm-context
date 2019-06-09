package com.douglei;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.service.Service;

/**
 * 
 * @author DougLei
 */
public class RegisterTest {
	public static void main(String[] args) {
		new SessionFactoryRegister().registerDefaultSessionFactory("com.douglei.service");
		
		ProxyBeanContext.getProxy(Service.class).add();
		
		
	}
}
