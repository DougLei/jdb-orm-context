package com.douglei.exception;

/**
 * 不支持的FunctionMapping类异常
 * @author DougLei
 */
public class UnsupportFunctionMappingClassException extends RuntimeException{
	private static final long serialVersionUID = -579675618429546578L;

	public UnsupportFunctionMappingClassException(String message) {
		super(message);
	}
}
