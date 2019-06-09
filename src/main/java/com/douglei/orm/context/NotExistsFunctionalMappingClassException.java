package com.douglei.orm.context;

/**
 * 不存在functionalMapping类异常
 * @author DougLei
 */
class NotExistsFunctionalMappingClassException extends RuntimeException{
	private static final long serialVersionUID = -6632621944851643707L;

	public NotExistsFunctionalMappingClassException(String functionalMappingClassName) {
		super("不存在class="+functionalMappingClassName+"的实例");
	}
	
	public NotExistsFunctionalMappingClassException(String functionalMappingClassName, Throwable e) {
		super("不存在class="+functionalMappingClassName+"的实例", e);
	}
}
