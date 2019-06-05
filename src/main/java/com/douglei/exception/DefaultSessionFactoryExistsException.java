package com.douglei.exception;

/**
 * 默认的SessionFactory已经存在异常
 * @author DougLei
 */
public class DefaultSessionFactoryExistsException extends RuntimeException{
	private static final long serialVersionUID = -5221412766799061903L;

	public DefaultSessionFactoryExistsException(String defaultSessionFactoryId) {
		super("已经存在默认的jdb-orm SessionFactory实例, id=" + defaultSessionFactoryId);
	}
}
