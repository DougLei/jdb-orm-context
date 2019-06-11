package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.orm.context.nmc.NecessaryMappingConfiguration;

/**
 * 
 * @author DougLei
 */
class SessionFactoryAndnecessaryMappingConfigurationLinkContext {
	private static Map<String, List<String>> SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK;// SessionFactory和NecessaryMappingConfiguration的关系, 即哪些SessionFactory关联了哪些NecessaryMappingConfiguration
	
	/**
	 * 是否存在关联
	 * @param sessionFactoryId
	 * @param necessaryMappingConfigurationClassName
	 * @return
	 */
	private static boolean existsLink(String sessionFactoryId, String necessaryMappingConfigurationClassName) {
		if(SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK != null) {
			List<String> necessaryMappingConfigurationClassNameList = SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK.get(sessionFactoryId);
			if(necessaryMappingConfigurationClassNameList != null && necessaryMappingConfigurationClassNameList.size() > 0) {
				return necessaryMappingConfigurationClassNameList.contains(necessaryMappingConfigurationClassName);
			}
		}
		return false;
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param necessaryMappingConfiguration
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, NecessaryMappingConfiguration necessaryMappingConfiguration) {
		return addLink(sessionFactoryId, necessaryMappingConfiguration.getClass().getName());
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param necessaryMappingConfigurationClassName
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, String necessaryMappingConfigurationClassName) {
		if(existsLink(sessionFactoryId, necessaryMappingConfigurationClassName)) {
			return false;
		}
		List<String> necessaryMappingConfigurationClassNameList = null;
		if(SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK == null) {
			SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK = new HashMap<String, List<String>>(8);
		}else {
			necessaryMappingConfigurationClassNameList = SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK.get(sessionFactoryId);
		}
		if(necessaryMappingConfigurationClassNameList == null) {
			necessaryMappingConfigurationClassNameList = new ArrayList<String>(10);
			SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK.put(sessionFactoryId, necessaryMappingConfigurationClassNameList);
		}
		necessaryMappingConfigurationClassNameList.add(necessaryMappingConfigurationClassName);
		return true;
	}
	
	/**
	 * 移除关联
	 * @param sessionFactoryId
	 * @param necessaryMappingConfigurationClassName
	 * @return 是否remove成功
	 */
	static boolean removeLink(String sessionFactoryId, String necessaryMappingConfigurationClassName) {
		if(existsLink(sessionFactoryId, necessaryMappingConfigurationClassName)) {
			SESSION_FACTORY_AND_NECESSARY_MAPPING_CONFIGURATION_LINK.get(sessionFactoryId).remove(necessaryMappingConfigurationClassName);
			return true;
		}
		return false;
	}
}
