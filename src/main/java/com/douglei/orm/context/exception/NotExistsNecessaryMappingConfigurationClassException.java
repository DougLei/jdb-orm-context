package com.douglei.orm.context.exception;

/**
 * 不存在NecessaryMappingConfiguration类异常
 * @author DougLei
 */
public class NotExistsNecessaryMappingConfigurationClassException extends RuntimeException{
	private static final long serialVersionUID = -522709403531972512L;

	public NotExistsNecessaryMappingConfigurationClassException(String necessaryMappingConfigurationClassName) {
		super("不存在class="+necessaryMappingConfigurationClassName+"的实例");
	}
	
	public NotExistsNecessaryMappingConfigurationClassException(String necessaryMappingConfigurationClassName, Throwable e) {
		super("不存在class="+necessaryMappingConfigurationClassName+"的实例", e);
	}
}
