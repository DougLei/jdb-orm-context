package com.douglei.exception;

/**
 * 重复的SessionFactory异常
 * @author DougLei
 */
public class RepeatedSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -3390034632023663009L;

	public RepeatedSessionFactoryException(String sessionFactoryId) {
		super("已存在id=["+sessionFactoryId+"]的SessionFactory实例");
	}
}
