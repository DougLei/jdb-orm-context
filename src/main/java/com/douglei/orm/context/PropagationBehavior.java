package com.douglei.orm.context;

/**
 * 事物的传播行为
 * @author DougLei
 */
public enum PropagationBehavior {

	/**
	 * 需要事物: 如果当前存在连接, 则在连接上开启事务; 否则创建一个带事物的新连接
	 */
	REQUIRED,
	
	/**
	 * 需要事物: 无论当前存不存在连接, 连接是否开启事务, 都创建一个带事物的新连接
	 */
	REQUIRED_NEW,
	
	/**
	 * 不需要事物: 如果当前存在连接, 则直接使用; 否则创建一个不带事物的新连接
	 */
	SUPPORTS;
}
