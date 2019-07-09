package com.douglei.orm.context.exception;

/**
 * 禁止销毁默认的数据源异常
 * @author DougLei
 */
public class ProhibitDestroyDefaultSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -3349343348009717366L;

	public ProhibitDestroyDefaultSessionFactoryException(String sessionFactoryId) {
		super("禁止销毁id=["+sessionFactoryId+"]的默认数据源");
	}
}
