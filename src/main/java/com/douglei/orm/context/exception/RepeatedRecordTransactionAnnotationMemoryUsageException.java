package com.douglei.orm.context.exception;

import com.douglei.orm.context.transaction.component.TransactionAnnotationMemoryUsage;

/**
 * 重复记录Transaction注解使用情况异常
 * @author DougLei
 */
public class RepeatedRecordTransactionAnnotationMemoryUsageException extends RuntimeException{
	private static final long serialVersionUID = 6237556864796587201L;

	public RepeatedRecordTransactionAnnotationMemoryUsageException() {
		super("禁止通过["+TransactionAnnotationMemoryUsage.class.getName()+"]类, 重复setMemoryUsage");
	}
}
