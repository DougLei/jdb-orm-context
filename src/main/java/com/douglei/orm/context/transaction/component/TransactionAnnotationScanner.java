package com.douglei.orm.context.transaction.component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * Transaction注解扫描器
 * @author DougLei
 */
public class TransactionAnnotationScanner {
	private static final Logger logger = LoggerFactory.getLogger(TransactionAnnotationScanner.class);
	
	/**
	 * 根据指定的包路径, 扫描事务组件
	 * @param scanAll
	 * @param transactionComponentPackages
	 * @return 
	 */
	public static List<TransactionComponentEntity> scan(boolean scanAll, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			ClassScanner scanner = new ClassScanner();
			List<String> classes = scanner.multiScan(scanAll, transactionComponentPackages);
			if(!classes.isEmpty()) {
				List<TransactionComponentEntity> entities = null;
				
				Class<?> loadClass = null;
				Method[] declareMethods = null;
				
				for (String clz : classes) {
					loadClass = ClassLoadUtil.loadClass(clz);
					if(loadClass.getAnnotation(TransactionComponent.class) != null) {
						declareMethods = loadClass.getDeclaredMethods();
						
						if(declareMethods.length > 0) {
							TransactionComponentEntity entity = null;
							for (Method method : declareMethods) {
								if(method.getAnnotation(Transaction.class) != null) {
									if(entity == null) 
										entity = new TransactionComponentEntity(loadClass, declareMethods.length);
									entity.addMethod(method);
								}
							}
							
							if(entity != null) {
								if(entities == null) 
									entities = new LinkedList<TransactionComponentEntity>();
								entities.add(entity);
							}
						}
					}
				}
				
				if(entities != null) {
					return entities;
				}
			}
		}
		logger.info("在指定的事务组件包["+Arrays.toString(transactionComponentPackages)+"]中, 没有扫描到事务组件配置");
		return Collections.emptyList();
	}
}
