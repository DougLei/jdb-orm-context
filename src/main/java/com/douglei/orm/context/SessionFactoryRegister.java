package com.douglei.orm.context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.impl.xml.XmlConfiguration;
import com.douglei.orm.context.exception.DefaultSessionFactoryExistsException;
import com.douglei.orm.context.exception.SessionFactoryRegistrationException;
import com.douglei.orm.context.exception.TooManyInstanceException;
import com.douglei.orm.context.exception.UnRegisterDefaultSessionFactoryException;
import com.douglei.orm.context.necessary.mapping.configuration.NecessaryMappingConfiguration;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * jdb-orm 的SessionFactory注册器
 * <p><b>注意: 该类只支持创建一个实例</b></p>
 * @author DougLei
 */
public final class SessionFactoryRegister {
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
	 * @param scanTransactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(String... scanTransactionComponentPackages) {
		return registerDefaultSessionFactoryByConfigurationFile(Configuration.DEFAULT_CONF_FILE, scanTransactionComponentPackages);
	}
	
	/**
	 * 【必须的数据源】使用指定的配置文件path注册默认的jdb-orm Configuration实例
	 * @param configurationFile
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactoryByConfigurationFile(String configurationFile, String... transactionComponentPackages) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		registerDefaultSessionFactory = true;
		SessionFactory sessionFactory = new XmlConfiguration(configurationFile).buildSessionFactory();
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		
		scanTransactionComponent(transactionComponentPackages);
		return sessionFactory;
	}
	
	/**
	 * 根据包路径扫描事务组件
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 */
	private void scanTransactionComponent(String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			List<TransactionProxyEntity> transactionProxyEntities = TransactionAnnotationMemoryUsage.scanTransactionComponent(transactionComponentPackages);
			for (TransactionProxyEntity transactionProxyEntity : transactionProxyEntities) {
				ProxyBeanContext.createAndAddProxy(transactionProxyEntity.getTransactionClass(), new TransactionProxyInterceptor(transactionProxyEntity.getTransactionClass(), transactionProxyEntity.getTransactionAnnotationMethods()));
			}
		}
	}

	// --------------------------------------------------------------------------------------------
	// 【多数据源】注册SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 【多数据源】使用指定的配置文件path注册jdb-orm SessionFactory实例
	 * @param configurationFile
	 * @return
	 */
	public SessionFactory registerSessionFactoryByConfigurationFile(String configurationFile) {
		if(registerDefaultSessionFactory) {
			return registerSessionFactoryByConfigurationInputStream(SessionFactoryRegister.class.getClassLoader().getResourceAsStream(configurationFile));
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
	// 操作SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 获取SessionFactory
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return SessionFactoryContext.getSessionFactory();
	}
	
	/**
	 * 销毁SessionFactory
	 * @param sessionFactoryId
	 */
	public void destroySessionFactory(String sessionFactoryId) {
		SessionFactoryContext.destroySessionFactory(sessionFactoryId);
	}
	
	/**
	 * 设置要操作的sessionFactoryId
	 * @param sessionFactoryId
	 */
	public void setSessionFactoryId(String sessionFactoryId) {
		SessionFactoryId4CurrentThread.setSessionFactoryId4CurrentThread(sessionFactoryId);
	}
	
	// --------------------------------------------------------------------------------------------
	// 操作necessaryMappingConfiguration
	// --------------------------------------------------------------------------------------------
	/**
	 * 给SessionFactory添加necessaryMappingConfiguration
	 * @param necessaryMappingConfiguration
	 */
	public void addNecessaryMappingConfiguration(NecessaryMappingConfiguration necessaryMappingConfiguration) {
		SessionFactoryContext.addNecessaryMappingConfiguration(necessaryMappingConfiguration);
	}
	
	/**
	 * 给SessionFactory添加necessaryMappingConfiguration
	 * @param necessaryMappingConfigurationClassName
	 */
	public void addNecessaryMappingConfiguration(String necessaryMappingConfigurationClassName) {
		SessionFactoryContext.addNecessaryMappingConfiguration(necessaryMappingConfigurationClassName);
	}
	
	/**
	 * 从SessionFactory移除necessaryMappingConfiguration
	 * @param necessaryMappingConfigurationClassName
	 */
	public void removeNecessaryMappingConfiguration(String necessaryMappingConfigurationClassName) {
		SessionFactoryContext.removeNecessaryMappingConfiguration(necessaryMappingConfigurationClassName);
	}
}
