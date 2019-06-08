package com.douglei;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.configuration.impl.xml.XmlConfiguration;
import com.douglei.exception.DefaultSessionFactoryExistsException;
import com.douglei.exception.SessionFactoryRegistrationException;
import com.douglei.exception.TooManyInstanceException;
import com.douglei.exception.UnRegisterDefaultSessionFactoryException;
import com.douglei.func.mapping.FunctionalMapping;
import com.douglei.instances.scanner.ClassScanner;
import com.douglei.sessionfactory.SessionFactory;
import com.douglei.transaction.Transaction;
import com.douglei.transaction.TransactionProxyInterceptor;
import com.douglei.utils.reflect.ClassLoadUtil;

/**
 * jdb-orm 的SessionFactory注册器
 * <p><b>注意: 该类只支持创建一个实例</b></p>
 * @author DougLei
 */
public class SessionFactoryRegister {
	private static final String DEFAULT_JDB_ORM_CONF_FILE_PATH = "jdb-orm.conf.xml";
	private boolean registerDefaultSessionFactory;// 是否注册过默认SessionFactory
	private static short instanceCount = 0;// 实例化次数
	
	public SessionFactoryRegister() {
		if(instanceCount > 0) {
			throw new TooManyInstanceException(SessionFactoryRegister.class.getName() + ", 只能创建一个实例, 请妥善处理创建出的实例, 保证其在项目中处于全局范围");
		}
		instanceCount=1;
	}
	
	// --------------------------------------------------------------------------------------------
	// 【必须的数据源】注册默认的SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 【必须的数据源】使用默认的配置文件path注册默认的jdb-orm SessionFactory实例
	 * @param scanTransactionPackages 要扫描事务的包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(String... scanTransactionPackages) {
		return registerDefaultSessionFactory(DEFAULT_JDB_ORM_CONF_FILE_PATH, scanTransactionPackages);
	}
	
	/**
	 * 【必须的数据源】使用指定的配置文件path注册默认的jdb-orm Configuration实例
	 * @param configurationFilePath
	 * @param scanTransactionPackages 要扫描事务的包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(String configurationFilePath, String... scanTransactionPackages) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		registerDefaultSessionFactory = true;
		SessionFactory sessionFactory = new XmlConfiguration(configurationFilePath).buildSessionFactory();
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		
		scanTransactionAnnotation(scanTransactionPackages);
		return sessionFactory;
	}
	
	/**
	 * 根据包路径扫描事务
	 * @param scanTransactionPackages 要扫描事务的包路径
	 */
	private void scanTransactionAnnotation(String... scanTransactionPackages) {
		if(scanTransactionPackages != null && scanTransactionPackages.length > 0) {
			ClassScanner cs = new ClassScanner();
			List<String> classes = cs.multiScan(scanTransactionPackages);
			if(classes.size() > 0) {
				Class<?> loadClass = null;
				Method[] declareMethods = null;
				List<Method> transactionAnnotationMethods = new ArrayList<Method>();
				
				for (String clz : classes) {
					loadClass = ClassLoadUtil.loadClass(clz);
					declareMethods = loadClass.getDeclaredMethods();
					if(declareMethods.length > 0) {
						for (Method dm : declareMethods) {
							if(dm.getAnnotation(Transaction.class) != null) {
								transactionAnnotationMethods.add(dm);
							}
						}
						
						if(transactionAnnotationMethods.size() > 0) {
							ProxyBeanContext.createProxyBean(loadClass, new TransactionProxyInterceptor(transactionAnnotationMethods));
							transactionAnnotationMethods.clear();
						}
					}
				}
				transactionAnnotationMethods = null;
			}
			cs.destroy();
		}
	}
	
	// --------------------------------------------------------------------------------------------
	// 【多数据源】注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 【多数据源】使用指定的配置文件path注册jdb-orm SessionFactory实例
	 * @param configurationFilePath
	 * @return
	 */
	public SessionFactory registerSessionFactoryByConfigurationFilePath(String configurationFilePath) {
		if(registerDefaultSessionFactory) {
			return registerSessionFactoryByConfigurationInputStream(SessionFactoryRegister.class.getClassLoader().getResourceAsStream(configurationFilePath));
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 【多数据源】使用指定的配置文件content注册jdb-orm SessionFactory实例
	 * @param configurationContent
	 * @return
	 */
	public SessionFactory registerSessionFactoryByConfigurationContent(String configurationContent) {
		if(registerDefaultSessionFactory) {
			try {
				return registerSessionFactoryByConfigurationInputStream(new ByteArrayInputStream(configurationContent.getBytes("utf-8")));
			} catch (UnsupportedEncodingException e) {
				throw new SessionFactoryRegistrationException("将configurationContent=["+configurationContent+"]转换为utf-8编码格式的byte数组时出现异常", e);
			}
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	/**
	 * 【多数据源】使用指定的配置文件input流注册jdb-orm SessionFactory实例
	 * @param in
	 * @return
	 */
	public SessionFactory registerSessionFactoryByConfigurationInputStream(InputStream in) {
		if(registerDefaultSessionFactory) {
			SessionFactory sessionFactory = new XmlConfiguration(in).buildSessionFactory();
			SessionFactoryContext.registerSessionFactory(sessionFactory);
			return sessionFactory;
		}
		throw new UnRegisterDefaultSessionFactoryException();
	}
	
	// --------------------------------------------------------------------------------------------
	// 获取SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return SessionFactoryContext.getSessionFactory();
	}
	
	// --------------------------------------------------------------------------------------------
	// 销毁SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 */
	public void destroySessionFactory(String sessionFactoryId) {
		SessionFactoryContext.destroySessionFactory(sessionFactoryId);
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作functionalMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 给SessionFactory添加functionalMapping
	 * @param functionalMapping
	 */
	public void addfunctionalMapping(FunctionalMapping functionalMapping) {
		SessionFactoryContext.addfunctionalMapping(functionalMapping);
	}
	
	/**
	 * 给SessionFactory添加functionalMapping
	 * @param functionalMappingClassName
	 */
	public void addfunctionalMapping(String functionalMappingClassName) {
		SessionFactoryContext.addfunctionalMapping(functionalMappingClassName);
	}
	
	/**
	 * 从SessionFactory移除functionalMapping
	 * @param functionalMappingClassName
	 */
	public void removefunctionalMapping(String functionalMappingClassName) {
		SessionFactoryContext.removefunctionalMapping(functionalMappingClassName);
	}
}
