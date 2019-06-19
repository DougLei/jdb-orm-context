package com.douglei.orm.context.exception;

import java.util.Arrays;

/**
 * 没有找到Transaction组件异常
 * @author DougLei
 */
public class NotFoundTransactionComponentConfigurationException extends RuntimeException{
	private static final long serialVersionUID = 91039541681959693L;

	public NotFoundTransactionComponentConfigurationException(String... transactionComponentPackages) {
		super("在指定的事务组件包["+Arrays.toString(transactionComponentPackages)+"]中, 没有扫描到事务组件配置, 请检查");
	}
}
