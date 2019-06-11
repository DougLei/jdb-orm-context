package com.douglei.orm.context.exception;

/**
 * 不支持的NecessaryMappingConfiguration类异常
 * @author DougLei
 */
public class UnsupportNecessaryMappingConfigurationClassException extends RuntimeException{
	private static final long serialVersionUID = 8865061288872483453L;

	public UnsupportNecessaryMappingConfigurationClassException(String message) {
		super(message);
	}
}
