package com.douglei.orm.context;

/**
 * 数据库连接事物的传播行为
 * @author DougLei
 */
public enum PropagationBehavior {

	/**
	 * 需要事物:  如果当前连接存在事物, 则使用该连接, 否则开启一个新的带事物的连接
	 */
	REQUIRED,
	
	/**
	 * 需要事物: 无论当前连接存不存在事物, 都创建一个新的带事物的连接
	 */
	REQUIRED_NEW,
	
	/**
	 * 不需要事物: 如果存在连接, 不论该连接是否存在事物, 都直接使用; 如果不存在连接, 则开启一个不带事物的连接
	 */
	SUPPORTS;
}
