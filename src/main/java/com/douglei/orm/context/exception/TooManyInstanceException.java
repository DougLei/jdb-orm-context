package com.douglei.orm.context.exception;

/**
 * 过多实例异常
 * @author DougLei
 */
public class TooManyInstanceException extends RuntimeException{
	private static final long serialVersionUID = -7370061824946333807L;

	public TooManyInstanceException(String message) {
		super(message);
	}
}
