package com.douglei.orm.context;

/**
 * 没有注册默认的SessionFactory异常
 * @author DougLei
 */
class UnRegisterDefaultSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 3029994298425277204L;

	public UnRegisterDefaultSessionFactoryException() {
		super("没有注册默认的SessionFactory");
	}
}
