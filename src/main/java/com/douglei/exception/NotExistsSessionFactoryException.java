package com.douglei.exception;

/**
 * 不存在SessionFactory异常
 * @author DougLei
 */
public class NotExistsSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = 2147716848356535626L;

	public NotExistsSessionFactoryException(String sessionFactoryId) {
		super("不存在id="+sessionFactoryId+"的SessionFactory实例");
	}
}
