package com.douglei.orm.context.transaction.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author DougLei
 */
@Target(ElementType.TYPE) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface TransactionComponent {
	
	/**
	 * 设置组件的名称; 默认名称为类的SimpleName, 首字母小写
	 * @return
	 */
	String value() default "";
}