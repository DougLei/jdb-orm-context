package com.douglei.orm.context.exception;

/**
 * 没有注册多个SessionFactory异常
 * @author DougLei
 */
public class UnRegisterMultipleSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 5283092041571131585L;

	public UnRegisterMultipleSessionFactoryException(String message) {
		super(message);
	}
}
