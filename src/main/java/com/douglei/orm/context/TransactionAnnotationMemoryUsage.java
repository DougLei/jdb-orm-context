package com.douglei.orm.context;

import com.douglei.orm.context.exception.RepeatedRecordTransactionAnnotationMemoryUsageException;

/**
 * Transaction注解的使用情况, 即记录是否使用Transaction注解
 * @author DougLei
 */
public class TransactionAnnotationMemoryUsage {
	private static boolean isRecord;
	private static boolean use;
	private static boolean unUse;
	
	/**
	 * 记录Transaction注解的使用情况
	 * @param isUse
	 */
	public static void setMemoryUsage(boolean isUse) {
		if(isRecord) {
			throw new RepeatedRecordTransactionAnnotationMemoryUsageException();
		}
		isRecord = true;
		use = isUse;
		unUse = !isUse;
	}
	
	public static boolean isUse() {
		return use;
	}
	public static boolean unUse() {
		return unUse;
	}
}
