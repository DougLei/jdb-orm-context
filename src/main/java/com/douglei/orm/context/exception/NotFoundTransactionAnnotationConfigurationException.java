package com.douglei.orm.context.exception;

import java.util.Arrays;

import com.douglei.orm.context.Transaction;

/**
 * 没有找到Transaction注解配置异常
 * @author DougLei
 */
public class NotFoundTransactionAnnotationConfigurationException extends RuntimeException{
	private static final long serialVersionUID = 9211741339479718935L;

	public NotFoundTransactionAnnotationConfigurationException(String... scanTransactionPackages) {
		super("在指定的方法["+Arrays.toString(scanTransactionPackages)+"]中, 没有扫描到"+Transaction.class.getName()+"注解, 请检查你的配置");
	}
}
