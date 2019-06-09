package com.douglei.orm.context;

/**
 * 重复扫描过事物注解异常
 * @author DougLei
 */
class RepeatedScanTransactionAnnotationException extends RuntimeException{
	private static final long serialVersionUID = 7106910125487151571L;

	public RepeatedScanTransactionAnnotationException() {
		super("已经扫描过事物注解, 不支持多次扫描");
	}
}
