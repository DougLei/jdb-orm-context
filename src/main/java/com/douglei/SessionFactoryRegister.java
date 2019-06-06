package com.douglei;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.douglei.configuration.impl.xml.XmlConfiguration;
import com.douglei.exception.DefaultSessionFactoryExistsException;
import com.douglei.exception.SessionFactoryRegistrationException;
import com.douglei.exception.TooManyInstanceException;
import com.douglei.exception.UnRegisterDefaultSessionFactoryException;
import com.douglei.func.mapping.FunctionalMapping;
import com.douglei.sessionfactory.SessionFactory;

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
			throw new TooManyInstanceException(SessionFactoryRegister.class.getName() + ", 只能创建一个实例, 请妥善处理之前创建出的实例, 保证其在项目中处于全局范围");
		}
		instanceCount=1;
	}
	
	// --------------------------------------------------------------------------------------------
	// 注册默认的SessionFactory
	// --------------------------------------------------------------------------------------------
	/**
	 * 使用默认的配置文件path注册默认的jdb-orm SessionFactory实例
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory() {
		return registerDefaultSessionFactory(DEFAULT_JDB_ORM_CONF_FILE_PATH);
	}
	
	/**
	 * 使用指定的配置文件path注册默认的jdb-orm Configuration实例
	 * @param configurationFilePath
	 * @return
	 */
	public SessionFactory registerDefaultSessionFactory(String configurationFilePath) {
		if(registerDefaultSessionFactory) {
			throw new DefaultSessionFactoryExistsException(SessionFactoryContext.getDefaultSessionFactory().getId());
		}
		registerDefaultSessionFactory = true;
		SessionFactory sessionFactory = new XmlConfiguration(configurationFilePath).buildSessionFactory();
		SessionFactoryContext.registerDefaultSessionFactory(sessionFactory);
		return sessionFactory;
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
