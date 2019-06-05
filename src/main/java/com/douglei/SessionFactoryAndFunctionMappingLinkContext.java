package com.douglei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.func.mapping.FunctionMapping;

/**
 * 
 * @author DougLei
 */
public class SessionFactoryAndFunctionMappingLinkContext {
	private static Map<String, List<String>> SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK;// SessionFactory和FunctionMapping的关系, 即哪些SessionFactory关联了哪些FunctionMapping
	
	/**
	 * 是否存在关联
	 * @param sessionFactoryId
	 * @param functionMappingClassName
	 * @return
	 */
	private static boolean existsLink(String sessionFactoryId, String functionMappingClassName) {
		if(SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK != null) {
			List<String> functionMappingClassNameList = SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId);
			if(functionMappingClassNameList != null && functionMappingClassNameList.size() > 0) {
				return functionMappingClassNameList.contains(functionMappingClassName);
			}
		}
		return false;
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param functionMapping
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, FunctionMapping functionMapping) {
		return addLink(sessionFactoryId, functionMapping.getClass().getName());
	}
	
	/**
	 * 添加关联
	 * @param sessionFactoryId
	 * @param functionMappingClassName
	 * @return 是否add成功
	 */
	static boolean addLink(String sessionFactoryId, String functionMappingClassName) {
		if(existsLink(sessionFactoryId, functionMappingClassName)) {
			return false;
		}
		List<String> functionMappingClassNameList = null;
		if(SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK == null) {
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK = new HashMap<String, List<String>>(8);
		}else {
			functionMappingClassNameList = SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId);
		}
		if(functionMappingClassNameList == null) {
			functionMappingClassNameList = new ArrayList<String>(10);
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.put(sessionFactoryId, functionMappingClassNameList);
		}
		functionMappingClassNameList.add(functionMappingClassName);
		return true;
	}
	
	/**
	 * 移除关联
	 * @param sessionFactoryId
	 * @param functionMappingClassName
	 * @return 是否remove成功
	 */
	static boolean removeLink(String sessionFactoryId, String functionMappingClassName) {
		if(existsLink(sessionFactoryId, functionMappingClassName)) {
			SESSION_FACTORY_AND_FUNCTION_MAPPING_LINK.get(sessionFactoryId).remove(functionMappingClassName);
			return true;
		}
		return false;
	}
}
