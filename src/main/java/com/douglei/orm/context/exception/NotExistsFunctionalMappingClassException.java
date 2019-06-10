package com.douglei.orm.context.exception;

/**
 * 不存在functionalMapping类异常
 * @author DougLei
 */
public class NotExistsFunctionalMappingClassException extends RuntimeException{
	private static final long serialVersionUID = 6814235087732615028L;

	public NotExistsFunctionalMappingClassException(String functionalMappingClassName) {
		super("不存在class="+functionalMappingClassName+"的实例");
	}
	
	public NotExistsFunctionalMappingClassException(String functionalMappingClassName, Throwable e) {
		super("不存在class="+functionalMappingClassName+"的实例", e);
	}
}
