package com.douglei.orm.context.exception;

/**
 * 没有启用多SessionFactory异常
 * @author DougLei
 */
public class NotEnabledMultipleSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 4485735763234996113L;

	public NotEnabledMultipleSessionFactoryException(String message) {
		super(message);
	}
}
