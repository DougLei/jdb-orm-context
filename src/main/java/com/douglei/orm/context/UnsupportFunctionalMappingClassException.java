package com.douglei.orm.context;

/**
 * 不支持的functionalMapping类异常
 * @author DougLei
 */
class UnsupportFunctionalMappingClassException extends RuntimeException{
	private static final long serialVersionUID = 5677376643272202292L;

	public UnsupportFunctionalMappingClassException(String message) {
		super(message);
	}
}
