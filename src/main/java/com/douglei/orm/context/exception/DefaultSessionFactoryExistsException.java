package com.douglei.orm.context.exception;

/**
 * 默认的SessionFactory已经存在异常
 * @author DougLei
 */
public class DefaultSessionFactoryExistsException extends RuntimeException{
	private static final long serialVersionUID = 7234408628511512025L;

	public DefaultSessionFactoryExistsException(String defaultSessionFactoryId) {
		super("已经存在默认的jdb-orm SessionFactory实例, id=" + defaultSessionFactoryId);
	}
}
