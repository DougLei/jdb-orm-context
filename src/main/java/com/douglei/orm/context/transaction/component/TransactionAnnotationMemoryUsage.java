package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.douglei.orm.context.exception.NotFoundTransactionComponentConfigurationException;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * Transaction注解的使用情况, 即记录是否使用Transaction注解
 * @author DougLei
 */
public class TransactionAnnotationMemoryUsage {
	// 默认是没有被使用的
	private static boolean use;
	private static boolean unUse = true;
	
	/**
	 * 设置/标识Transaction注解被使用
	 * @param isUse
	 */
	private static void setIsUse() {
		use = true;
		unUse = false;
	}
	
	public static boolean isUse() {
		return use;
	}
	public static boolean unUse() {
		return unUse;
	}
	
	/**
	 * 根据指定的包路径, 扫描事务组件
	 * @param transactionComponentPackages
	 * @return 返回扫描到的TransactionClass集合
	 */
	public static List<TransactionComponentProxyEntity> scanTransactionComponent(String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			ClassScanner cs = new ClassScanner(true);
			List<String> classes = cs.multiScan(transactionComponentPackages);
			if(classes.size() > 0) {
				List<TransactionComponentProxyEntity> transactionComponentProxyEntities = null;
				
				Class<?> loadClass = null;
				Method[] declareMethods = null;
				
				for (String clz : classes) {
					loadClass = ClassLoadUtil.loadClass(clz);
					if(loadClass.getAnnotation(TransactionComponent.class) != null) {
						declareMethods = loadClass.getDeclaredMethods();
						
						if(declareMethods.length > 0) {
							TransactionComponentProxyEntity transactionProxyEntity = null;
							for (Method dm : declareMethods) {
								if(dm.getAnnotation(Transaction.class) != null) {
									if(transactionProxyEntity == null) {
										transactionProxyEntity = new TransactionComponentProxyEntity(loadClass, declareMethods.length);
									}
									transactionProxyEntity.addMethod(dm);
								}
							}
							
							if(transactionProxyEntity != null) {
								if(transactionComponentProxyEntities == null) {
									transactionComponentProxyEntities = new LinkedList<TransactionComponentProxyEntity>();
								}
								transactionComponentProxyEntities.add(transactionProxyEntity);
							}
						}
					}
				}
				
				cs.destroy();
				
				if(transactionComponentProxyEntities != null) {
					setIsUse();
					return transactionComponentProxyEntities;
				}
			}
		}
		throw new NotFoundTransactionComponentConfigurationException(transactionComponentPackages);
	}
}
