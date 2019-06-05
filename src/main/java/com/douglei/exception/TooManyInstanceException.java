package com.douglei.exception;

/**
 * 过多实例异常
 * @author DougLei
 */
public class TooManyInstanceException extends RuntimeException{
	private static final long serialVersionUID = 4887336138055327270L;

	public TooManyInstanceException(String message) {
		super(message);
	}
}
