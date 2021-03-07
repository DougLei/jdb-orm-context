package com.douglei.orm.context;

/**
 * id重复异常
 * @author DougLei
 */
public class IdRepeatedException extends Exception{

	public IdRepeatedException(String id) {
		super("已经存在id为"+id+"的实例");
	}
}
