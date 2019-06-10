package com.douglei.orm.context.exception;

/**
 * 不支持的functionalMapping类异常
 * @author DougLei
 */
public class UnsupportFunctionalMappingClassException extends RuntimeException{
	private static final long serialVersionUID = 273447500784924676L;

	public UnsupportFunctionalMappingClassException(String message) {
		super(message);
	}
}
