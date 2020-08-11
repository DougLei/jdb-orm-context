package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.douglei.aop.ProxyMethod;
import com.douglei.orm.context.exception.NotFoundTransactionComponentConfigurationException;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * Transaction注解的使用情况, 即记录是否使用Transaction注解
 * @author DougLei
 */
public class TransactionAnnotationMemoryUsage {

	/**
	 * 根据指定的包路径, 扫描事务组件
	 * @param searchAll
	 * @param transactionComponentPackages
	 * @return 返回扫描到的TransactionClass集合
	 */
	public static List<TransactionComponentEntity> scanTransactionComponent(boolean searchAll, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			ClassScanner cs = new ClassScanner();
			List<String> classes = cs.multiScan(searchAll, transactionComponentPackages);
			if(classes.size() > 0) {
				List<TransactionComponentEntity> transactionComponentEntities = null;
				
				Class<?> loadClass = null;
				Method[] declareMethods = null;
				
				for (String clz : classes) {
					loadClass = ClassLoadUtil.loadClass(clz);
					if(loadClass.getAnnotation(TransactionComponent.class) != null) {
						declareMethods = loadClass.getDeclaredMethods();
						
						if(declareMethods.length > 0) {
							TransactionComponentEntity transactionComponentEntity = null;
							for (Method dm : declareMethods) {
								if(dm.getAnnotation(Transaction.class) != null) {
									if(transactionComponentEntity == null) {
										transactionComponentEntity = new TransactionComponentEntity(loadClass, declareMethods.length);
									}
									transactionComponentEntity.addMethod(new ProxyMethod(dm));
								}
							}
							
							if(transactionComponentEntity != null) {
								if(transactionComponentEntities == null) {
									transactionComponentEntities = new LinkedList<TransactionComponentEntity>();
								}
								transactionComponentEntities.add(transactionComponentEntity);
							}
						}
					}
				}
				
				cs.destroy();
				
				if(transactionComponentEntities != null) {
					return transactionComponentEntities;
				}
			}
		}
		throw new NotFoundTransactionComponentConfigurationException(transactionComponentPackages);
	}
}
