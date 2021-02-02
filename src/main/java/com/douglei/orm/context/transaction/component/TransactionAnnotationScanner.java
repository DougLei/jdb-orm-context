package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.douglei.tools.file.scanner.impl.ClassScanner;
import com.douglei.tools.reflect.ClassUtil;

/**
 * Transaction注解扫描器
 * @author DougLei
 */
public class TransactionAnnotationScanner {
	
	/**
	 * 根据指定的包路径, 扫描事务组件
	 * @param scanAll
	 * @param transactionComponentPackages
	 * @return 
	 */
	public static List<TransactionComponentEntity> scan(boolean scanAll, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			List<String> classpaths = new ClassScanner().multiScan(scanAll, transactionComponentPackages);
			if(!classpaths.isEmpty()) {
				List<TransactionComponentEntity> entities = null;
				
				Class<?> clazz = null;
				TransactionComponent transactionComponent = null;
				TransactionComponentEntity entity = null;
				for (String classpath : classpaths) {
					clazz = ClassUtil.loadClass2(classpath);
					transactionComponent = clazz.getAnnotation(TransactionComponent.class);
					
					if(transactionComponent != null) {
						entity = new TransactionComponentEntity(transactionComponent.value(), clazz);
						do {
							for (Method method : clazz.getDeclaredMethods()) {
								if(!method.isAnnotationPresent(Transaction.class))
									continue;
								entity.addMethod(method);
							}
							clazz = clazz.getSuperclass();
						}while(clazz != Object.class);
						
						if(entity.getMethods() == null)
							throw new NullPointerException("["+clazz.getName()+"]类被["+TransactionComponent.class.getName()+"]注解修饰, 但是类中却没有任何方法有["+Transaction.class.getName()+"]注解");

						if(entities == null) 
							entities = new LinkedList<TransactionComponentEntity>();
						entities.add(entity);
					}
				}
				if(entities != null) 
					return entities;
			}
		}
		throw new IllegalArgumentException("在指定的事务组件包["+Arrays.toString(transactionComponentPackages)+"]中, 没有扫描到["+TransactionComponent.class.getName()+"]配置");
	}
}
