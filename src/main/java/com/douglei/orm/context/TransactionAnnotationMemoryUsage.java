package com.douglei.orm.context;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.douglei.orm.context.exception.NotFoundTransactionAnnotationConfigurationException;
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
	 * 根据指定的包路径, 扫描事务注解
	 * @param scanTransactionPackages
	 * @return 返回扫描到的TransactionClass集合
	 */
	public static List<TransactionProxyEntity> scanTransactionAnnotation(String... scanTransactionPackages) {
		if(scanTransactionPackages.length > 0) {
			ClassScanner cs = new ClassScanner();
			List<String> classes = cs.multiScan(scanTransactionPackages);
			if(classes.size() > 0) {
				List<TransactionProxyEntity> transactionProxyEntities = null;
				
				Class<?> loadClass = null;
				Method[] declareMethods = null;
				
				for (String clz : classes) {
					loadClass = ClassLoadUtil.loadClass(clz);
					declareMethods = loadClass.getDeclaredMethods();
					
					if(declareMethods.length > 0) {
						TransactionProxyEntity transactionProxyEntity = null;
						for (Method dm : declareMethods) {
							if(dm.getAnnotation(Transaction.class) != null) {
								if(transactionProxyEntity == null) {
									transactionProxyEntity = new TransactionProxyEntity(loadClass, declareMethods.length);
								}
								transactionProxyEntity.addMethod(dm);
							}
						}
						
						if(transactionProxyEntity != null) {
							if(transactionProxyEntities == null) {
								transactionProxyEntities = new LinkedList<TransactionProxyEntity>();
							}
							transactionProxyEntities.add(transactionProxyEntity);
						}
					}
				}
				
				cs.destroy();
				
				if(transactionProxyEntities != null) {
					setIsUse();
					return transactionProxyEntities;
				}
			}
		}
		throw new NotFoundTransactionAnnotationConfigurationException(scanTransactionPackages);
	}
}
