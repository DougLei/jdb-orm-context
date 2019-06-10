package com.douglei.orm.context.exception;

/**
 * 重复的SessionFactory异常
 * @author DougLei
 */
public class RepeatedSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -4033372069453702144L;

	public RepeatedSessionFactoryException(String sessionFactoryId) {
		super("已存在id=["+sessionFactoryId+"]的SessionFactory实例");
	}
}
