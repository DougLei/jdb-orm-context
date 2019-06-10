package com.douglei.orm.context.spring;

import com.douglei.orm.context.SessionFactoryRegister;


/**
 * 
 * @author DougLei
 */
public class SessionFactoryRegisterHolder {
	private static final SessionFactoryRegister sessionFactoryRegister = new SessionFactoryRegister();
	
	/**
	 * 【必须配置】设置默认的SessionFactory
	 * @param defaultSessionFactoryConfigurationFilePath
	 */
	public void setDefaultSessionFactoryConfigurationFilePath(String defaultSessionFactoryConfigurationFilePath) {
		sessionFactoryRegister.registerDefaultSessionFactoryByConfigurationFilePath(defaultSessionFactoryConfigurationFilePath);
	}
	
	/**
	 * 【多数据源】设置SessionFactory
	 */
	public void setSessionFactoryConfigurationFilePath(String... sessionFactoryConfigurationFilePaths) {
		if(sessionFactoryConfigurationFilePaths != null && sessionFactoryConfigurationFilePaths.length > 0) {
			for (String configurationFilePath : sessionFactoryConfigurationFilePaths) {
				sessionFactoryRegister.registerSessionFactoryByConfigurationFilePath(configurationFilePath);
			}
		}
	}
	
	public SessionFactoryRegister getSessionFactoryRegister() {
		return sessionFactoryRegister;
	}
}
