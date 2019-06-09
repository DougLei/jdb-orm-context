package com.douglei.orm.context;

/**
 * 默认的SessionFactory已经存在异常
 * @author DougLei
 */
class DefaultSessionFactoryExistsException extends RuntimeException{
	private static final long serialVersionUID = -3264695652680610812L;

	public DefaultSessionFactoryExistsException(String defaultSessionFactoryId) {
		super("已经存在默认的jdb-orm SessionFactory实例, id=" + defaultSessionFactoryId);
	}
}
