package com.douglei.orm.context;

/**
 * 传播行为
 * @author DougLei
 */
public enum PropagationBehavior {

	/**
	 * 需要事物, 如果当前存在一个事物, 则使用该事物, 否则开启一个事物
	 */
	REQUIRED,
	
	/**
	 * 需要事物, 无论当前存不存在事物, 都创建一个新事物
	 */
	REQUIRED_NEW,
	
	/**
	 * 不需要事物, 但是如果当前存在一个事物, 则使用该事物, 如果不存在, 则开启一个不带事物的连接
	 */
	SUPPORTS;
}
