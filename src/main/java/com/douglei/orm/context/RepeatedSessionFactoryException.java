package com.douglei.orm.context;

/**
 * 重复的SessionFactory异常
 * @author DougLei
 */
class RepeatedSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -7123447056720798945L;

	public RepeatedSessionFactoryException(String sessionFactoryId) {
		super("已存在id=["+sessionFactoryId+"]的SessionFactory实例");
	}
}
