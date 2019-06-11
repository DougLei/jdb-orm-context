package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.context.exception.NotExistsNecessaryMappingConfigurationClassException;
import com.douglei.orm.context.exception.UnsupportNecessaryMappingConfigurationClassException;
import com.douglei.orm.context.nmc.NecessaryMappingConfiguration;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
class NecessaryMappingConfigurationContext {
	private static Map<String, NecessaryMappingConfiguration> NECESSARY_MAPPING_CONFIGURATIONS;// 功能映射集合
	
	// --------------------------------------------------------------------------------------------
	// 注册necessaryMappingConfiguration
	// --------------------------------------------------------------------------------------------
	/**
	 * 注册necessaryMappingConfiguration
	 * @param necessaryMappingConfiguration
	 */
	static void registerNecessaryMappingConfiguration(NecessaryMappingConfiguration necessaryMappingConfiguration) {
		String necessaryMappingConfigurationClassName = necessaryMappingConfiguration.getClass().getName();
		if(NECESSARY_MAPPING_CONFIGURATIONS == null) {
			NECESSARY_MAPPING_CONFIGURATIONS = new HashMap<String, NecessaryMappingConfiguration>(8);
		}else if(NECESSARY_MAPPING_CONFIGURATIONS.containsKey(necessaryMappingConfigurationClassName)) {
			return;
		}
		NECESSARY_MAPPING_CONFIGURATIONS.put(necessaryMappingConfigurationClassName, necessaryMappingConfiguration);
	}

	// --------------------------------------------------------------------------------------------
	// 获取necessaryMappingConfiguration
	// --------------------------------------------------------------------------------------------
	/**
	 * 根据necessaryMappingConfiguration ClassName获取对应的实例
	 * @param necessaryMappingConfigurationClassName
	 * @return
	 */
	static NecessaryMappingConfiguration getNecessaryMappingConfiguration(String necessaryMappingConfigurationClassName) {
		if(NECESSARY_MAPPING_CONFIGURATIONS == null) {
			NECESSARY_MAPPING_CONFIGURATIONS = new HashMap<String, NecessaryMappingConfiguration>(8);
		}else if(NECESSARY_MAPPING_CONFIGURATIONS.containsKey(necessaryMappingConfigurationClassName)) {
			return NECESSARY_MAPPING_CONFIGURATIONS.get(necessaryMappingConfigurationClassName);
		}
		
		Object obj = null;
		try {
			obj = ConstructorUtil.newInstance(necessaryMappingConfigurationClassName);
		} catch (Exception e) {
			throw new NotExistsNecessaryMappingConfigurationClassException(necessaryMappingConfigurationClassName, e);
		}
		if(!(obj instanceof NecessaryMappingConfiguration)) {
			throw new UnsupportNecessaryMappingConfigurationClassException("["+necessaryMappingConfigurationClassName+"]类必须继承["+NecessaryMappingConfiguration.class.getName()+"]");
		}
		NecessaryMappingConfiguration necessaryMappingConfiguration = (NecessaryMappingConfiguration) obj;
		registerNecessaryMappingConfiguration(necessaryMappingConfiguration);
		return necessaryMappingConfiguration;
	}

	// --------------------------------------------------------------------------------------------
	// 删除necessaryMappingConfiguration
	// --------------------------------------------------------------------------------------------
	/**
	 * 移除necessaryMappingConfiguration
	 * @param necessaryMappingConfigurationClassName
	 */
	static void removeNecessaryMappingConfiguration(String necessaryMappingConfigurationClassName) {
		if(NECESSARY_MAPPING_CONFIGURATIONS != null) {
			NECESSARY_MAPPING_CONFIGURATIONS.remove(necessaryMappingConfigurationClassName);
		}
	}
}
