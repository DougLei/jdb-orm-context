package com.douglei.orm.context.exception;

/**
 * 没有注册默认的SessionFactory异常
 * @author DougLei
 */
public class UnRegisterDefaultSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 2490066648063200536L;

	public UnRegisterDefaultSessionFactoryException() {
		super("没有注册默认的SessionFactory");
	}
}
