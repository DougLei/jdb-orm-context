package com.douglei.exception;

/**
 * 不存在FunctionMapping类异常
 * @author DougLei
 */
public class NotExistsFunctionMappingClassException extends RuntimeException{
	private static final long serialVersionUID = 3876206119834900181L;

	public NotExistsFunctionMappingClassException(String functionMappingClassName) {
		super("不存在class="+functionMappingClassName+"的实例");
	}
	
	public NotExistsFunctionMappingClassException(String functionMappingClassName, Throwable e) {
		super("不存在class="+functionMappingClassName+"的实例", e);
	}
}
