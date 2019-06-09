package com.douglei.orm.context;

/**
 * 不存在SessionFactory异常
 * @author DougLei
 */
class NotExistsSessionFactoryException extends RuntimeException{
	private static final long serialVersionUID = -5209608948005175810L;

	public NotExistsSessionFactoryException(String sessionFactoryId) {
		super("不存在id="+sessionFactoryId+"的SessionFactory实例");
	}
}
