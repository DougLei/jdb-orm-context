package com.douglei;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.douglei.core.dialect.TransactionIsolationLevel;

/**
 * 
 * @author DougLei
 */
@Target(ElementType.METHOD) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface Transaction {
	
	/**
	 * 传播行为
	 * @return
	 */
	PropagationBehavior propagationBehavior() default PropagationBehavior.REQUIRED;
	
	/**
	 * 是否开启事物
	 * @return
	 */
	boolean beginTransaction() default true;
	
	/**
	 * 事物隔离级别
	 * @return
	 */
	TransactionIsolationLevel transactionIsolationLevel() default TransactionIsolationLevel.DEFAULT;
}