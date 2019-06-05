package com.douglei.exception;

/**
 * SessionFactory注册异常
 * @author DougLei
 */
public class SessionFactoryRegistrationException extends RuntimeException{
	private static final long serialVersionUID = -8962583854227371379L;

	public SessionFactoryRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
