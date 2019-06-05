package com.douglei.exception;

/**
 * 没有注册默认的SessionFactory异常
 * @author DougLei
 */
public class UnRegisterDefaultSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 5586695792876150162L;

	public UnRegisterDefaultSessionFactoryException() {
		super("没有注册默认的SessionFactory");
	}
}
