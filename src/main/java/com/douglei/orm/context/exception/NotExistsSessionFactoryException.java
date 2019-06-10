package com.douglei.orm.context.exception;

/**
 * 不存在SessionFactory异常
 * @author DougLei
 */
public class NotExistsSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -4339727735271657879L;

	public NotExistsSessionFactoryException(String sessionFactoryId) {
		super("不存在id="+sessionFactoryId+"的SessionFactory实例");
	}
}
