package com.douglei.orm.context.exception;

/**
 * SessionFactory注册异常
 * @author DougLei
 */
public class SessionFactoryRegistrationException extends RuntimeException{
	private static final long serialVersionUID = 1337333366476257032L;
	
	public SessionFactoryRegistrationException(String message) {
		super(message);
	}
	public SessionFactoryRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
