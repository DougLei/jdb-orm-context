package com.douglei.orm.context.exception;

/**
 * 没有注册多个SessionFactory异常
 * @author DougLei
 */
public class UnRegisterMultipleSessionFactoriesException extends RuntimeException{
	private static final long serialVersionUID = 5756443644741768635L;

	public UnRegisterMultipleSessionFactoriesException(String message) {
		super(message);
	}
}
