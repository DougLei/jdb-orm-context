package com.douglei.orm.context;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.context.service.Service;

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
