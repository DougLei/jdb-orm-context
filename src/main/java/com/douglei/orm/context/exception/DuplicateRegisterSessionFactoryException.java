package com.douglei.orm.context.exception;

/**
 * 重复注册
 * @author DougLei
 */
public class DuplicateRegisterSessionFactoryException extends Exception{
	private static final long serialVersionUID = -3170601213121456085L;

	public DuplicateRegisterSessionFactoryException(String id) {
		super("已经存在id为"+id+"的SessionFactory实例, 注册失败");
	}
}
