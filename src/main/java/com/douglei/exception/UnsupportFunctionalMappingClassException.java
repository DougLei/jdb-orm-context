package com.douglei.exception;

/**
 * 不支持的functionalMapping类异常
 * @author DougLei
 */
public class UnsupportFunctionalMappingClassException extends RuntimeException{
	private static final long serialVersionUID = 5847099716009276118L;

	public UnsupportFunctionalMappingClassException(String message) {
		super(message);
	}
}
