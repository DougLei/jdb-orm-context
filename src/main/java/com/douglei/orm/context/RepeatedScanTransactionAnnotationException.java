package com.douglei.orm.context;

/**
 * 重复扫描Transaction注解异常
 * @author DougLei
 */
public class RepeatedScanTransactionAnnotationException extends RuntimeException{
	private static final long serialVersionUID = 8022272060877305727L;

	public RepeatedScanTransactionAnnotationException() {
		super(Transaction.class.getName() + " 注解禁止重复扫描");
	}
}
