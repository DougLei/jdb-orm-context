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
import com.douglei.orm.context.transaction.component.TransactionAnnotationMemoryUsage;
import com.douglei.orm.context.transaction.component.TransactionComponentProxyEntity;
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
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(String... transactionComponentPackages) {
		return registerDefaultSessionFactoryByConfigurationFile(Configuration.DEFAULT_CONF_FILE, false, transactionComponentPackages);
	}
	
	/**
	 * 【必须的数据源】使用默认的配置文件path注册默认的jdb-orm SessionFactory实例
	 * @param searchAllPath
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(boolean searchAllPath, String... transactionComponentPackages) {
		return registerDefaultSessionFactoryByConfigurationFile(Configuration.DEFAULT_CONF_FILE, searchAllPath, transactionComponentPackages);
	}
	
	/**
	 * 【必须的数据源】使用指定的配置文件path注册默认的jdb-orm Configuration实例
	 * @param configurationFile
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactoryByConfigurationFile(String configurationFile, String... transactionComponentPackages) {
		return registerDefaultSessionFactoryByConfigurationFile(configurationFile, false, transactionComponentPackages);
	}
	
	/**
	 * 【必须的数据源】使用指定的配置文件path注册默认的jdb-orm Configuration实例
	 * @param configurationFile
	 * @param searchAllPath
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactoryByConfigurationFile(String configurationFile, boolean searchAllPath, String... transactionComponentPackages) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		registerDefaultSessionFactory = true;
		SessionFactory sessionFactory = new XmlConfiguration(configurationFile).buildSessionFactory();
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		
		scanTransactionComponent(searchAllPath, transactionComponentPackages);
		return sessionFactory;
	}
	
	/**
	 * 根据包路径扫描事务组件
	 * @param searchAllPath
	 * @param transactionComponentPackages 要扫描事务组件包路径
	 */
	private void scanTransactionComponent(boolean searchAllPath, String... transactionComponentPackages) {
		if(transactionComponentPackages.length > 0) {
			List<TransactionComponentProxyEntity> transactionComponentProxyEntities = TransactionAnnotationMemoryUsage.scanTransactionComponent(searchAllPath, transactionComponentPackages);
			for (TransactionComponentProxyEntity transactionComponentProxyEntity : transactionComponentProxyEntities) {
				ProxyBeanContext.createAndAddProxy(transactionComponentProxyEntity.getTransactionComponentProxyBeanClass(), new TransactionProxyInterceptor(transactionComponentProxyEntity.getTransactionComponentProxyBeanClass(), transactionComponentProxyEntity.getTransactionMethods()));
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
}
