package com.douglei.orm.context;

/**
 * id重复异常
 * @author DougLei
 */
public class IdRepeatedException extends Exception{
	private static final long serialVersionUID = -929180829383634328L;

	public IdRepeatedException(String id) {
		super("已经存在id为"+id+"的实例");
	}
}
