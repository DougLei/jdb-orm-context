package com.douglei.orm.context.exception;

/**
 * SessionFactory注册异常
 * @author DougLei
 */
public class SessionFactoryRegistrationException extends RuntimeException{
	private static final long serialVersionUID = 6327748709685818623L;

	public SessionFactoryRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
