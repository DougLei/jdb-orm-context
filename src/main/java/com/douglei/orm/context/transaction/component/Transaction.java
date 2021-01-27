package com.douglei.orm.context.transaction.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.douglei.orm.configuration.environment.datasource.TransactionIsolationLevel;
import com.douglei.orm.context.PropagationBehavior;

/**
 * 
 * @author DougLei
 */
@Target(ElementType.METHOD) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface Transaction {
	
	/**
	 * 事物传播行为
	 * @return
	 */
	PropagationBehavior propagationBehavior() default PropagationBehavior.REQUIRED;
	
	/**
	 * 事物隔离级别
	 * @return
	 */
	TransactionIsolationLevel transactionIsolationLevel() default TransactionIsolationLevel.DEFAULT;
}