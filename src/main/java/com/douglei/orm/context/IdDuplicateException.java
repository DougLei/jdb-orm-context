package com.douglei.orm.context;

/**
 * id重复异常
 * @author DougLei
 */
public class IdDuplicateException extends Exception{
	private static final long serialVersionUID = -2320594230617906548L;

	public IdDuplicateException(String message) {
		super(message);
	}
}
