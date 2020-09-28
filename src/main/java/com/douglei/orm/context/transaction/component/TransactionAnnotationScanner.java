package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.aop.ProxyMethod;
import com.douglei.tools.instances.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * Transaction注解扫描器
 * @author DougLei
 */
public class TransactionAnnotationScanner {
	private static final Logger logger = LoggerFactory.getLogger(TransactionAnnotationScanner.class);
	
	/**
	 * 根据指定的包路径, 扫描事务组件
	 * @param searchAll
	 * @param transactionComponentPackages
	 * @return 
	 */
	public static List<TransactionComponentEntity> scan(boolean searchAll, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			ClassScanner scanner = new ClassScanner();
			List<String> classes = scanner.multiScan(searchAll, transactionComponentPackages);
			if(!classes.isEmpty()) {
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
				
				scanner.destroy();
				
				if(transactionComponentEntities != null) {
					return transactionComponentEntities;
				}
			}
		}
		logger.info("在指定的事务组件包["+Arrays.toString(transactionComponentPackages)+"]中, 没有扫描到事务组件配置");
		return Collections.emptyList();
	}
}
