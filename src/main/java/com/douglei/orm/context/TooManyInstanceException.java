package com.douglei.orm.context;

/**
 * 过多实例异常
 * @author DougLei
 */
class TooManyInstanceException extends RuntimeException{
	private static final long serialVersionUID = -4523240955847662188L;

	public TooManyInstanceException(String message) {
		super(message);
	}
}
