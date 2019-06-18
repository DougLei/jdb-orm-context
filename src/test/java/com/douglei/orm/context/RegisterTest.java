package com.douglei.orm.context;

import org.junit.Test;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.context.service.Service;

/**
 * 
 * @author DougLei
 */
public class RegisterTest {
	
	@Test
	public void test() {
		new SessionFactoryRegister().registerDefaultSessionFactory("com.douglei.orm.context.service");
		ProxyBeanContext.getProxy(Service.class).add();
	}
}
