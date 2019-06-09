package com.douglei.orm.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author DougLei
 */
class SessionFactoryAndfunctionalMappingLinkContext {
	private static Map<String, List<String>> SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK;// SessionFactory和functionalMapping的关系, 即哪些SessionFactory关联了哪些functionalMapping
	
	/**
	 * 是否存在关联
	 * @param sessionFactoryId
	 * @param functionalMappingClassName
	 * @return
	 */
	private static boolean existsLink(String sessionFactoryId, String functionalMappingClassName) {
		if(SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK != null) {
			List<String> functionalMappingClassNameList = SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId);
			if(functionalMappingClassNameList != null && functionalMappingClassNameList.size() > 0) {
				return functionalMappingClassNameList.contains(functionalMappingClassName);
			}
		}
		return false;
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param functionalMapping
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, FunctionalMapping functionalMapping) {
		return addLink(sessionFactoryId, functionalMapping.getClass().getName());
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param functionalMappingClassName
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, String functionalMappingClassName) {
		if(existsLink(sessionFactoryId, functionalMappingClassName)) {
			return false;
		}
		List<String> functionalMappingClassNameList = null;
		if(SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK == null) {
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK = new HashMap<String, List<String>>(8);
		}else {
			functionalMappingClassNameList = SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId);
		}
		if(functionalMappingClassNameList == null) {
			functionalMappingClassNameList = new ArrayList<String>(10);
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.put(sessionFactoryId, functionalMappingClassNameList);
		}
		functionalMappingClassNameList.add(functionalMappingClassName);
		return true;
	}
	
	/**
	 * 移除关联
	 * @param sessionFactoryId
	 * @param functionalMappingClassName
	 * @return 是否remove成功
	 */
	static boolean removeLink(String sessionFactoryId, String functionalMappingClassName) {
		if(existsLink(sessionFactoryId, functionalMappingClassName)) {
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId).remove(functionalMappingClassName);
			return true;
		}
		return false;
	}
}
