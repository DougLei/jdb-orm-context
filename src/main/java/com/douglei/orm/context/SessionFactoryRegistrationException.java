package com.douglei.orm.context;

/**
 * SessionFactory注册异常
 * @author DougLei
 */
class SessionFactoryRegistrationException extends RuntimeException{
	private static final long serialVersionUID = 7859331017530030338L;

	public SessionFactoryRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
