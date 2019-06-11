package com.douglei.orm.context;

/**
 * Transaction注解的使用情况, 即记录是否使用Transaction注解
 * @author DougLei
 */
public class TransactionAnnotationMemoryUsage {
	private static boolean use;
	private static boolean unUse;
	
	/**
	 * <pre>
	 * 	记录Transaction注解的使用情况
	 * 	该方法很重要, 禁止随意调用
	 * </pre>
	 * @param isUse
	 */
	public static void setMemoryUsage(boolean isUse) {
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
