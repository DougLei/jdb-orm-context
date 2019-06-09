package com.douglei.orm.context;

import java.util.HashMap;
import java.util.Map;

import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
class FunctionalMappingContext {
	private static Map<String, FunctionalMapping> FUNCTION_MAPPINGS;// 功能映射集合
	
	// --------------------------------------------------------------------------------------------
	// 注册functionalMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 注册functionalMapping
	 * @param functionalMapping
	 */
	static void registerfunctionalMapping(FunctionalMapping functionalMapping) {
		String functionalMappingClassName = functionalMapping.getClass().getName();
		if(FUNCTION_MAPPINGS == null) {
			FUNCTION_MAPPINGS = new HashMap<String, FunctionalMapping>(10);
		}else if(FUNCTION_MAPPINGS.containsKey(functionalMappingClassName)) {
			return;
		}
		FUNCTION_MAPPINGS.put(functionalMappingClassName, functionalMapping);
	}

	// --------------------------------------------------------------------------------------------
	// 获取functionalMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 根据functionalMapping ClassName获取对应的实例
	 * @param functionalMappingClassName
	 * @return
	 */
	static FunctionalMapping getfunctionalMapping(String functionalMappingClassName) {
		if(FUNCTION_MAPPINGS == null) {
			FUNCTION_MAPPINGS = new HashMap<String, FunctionalMapping>(10);
		}else if(FUNCTION_MAPPINGS.containsKey(functionalMappingClassName)) {
			return FUNCTION_MAPPINGS.get(functionalMappingClassName);
		}
		
		Object obj = null;
		try {
			obj = ConstructorUtil.newInstance(functionalMappingClassName);
		} catch (Exception e) {
			throw new NotExistsFunctionalMappingClassException(functionalMappingClassName, e);
		}
		if(!(obj instanceof FunctionalMapping)) {
			throw new UnsupportFunctionalMappingClassException("["+functionalMappingClassName+"]类必须继承["+FunctionalMapping.class.getName()+"]");
		}
		FunctionalMapping functionalMapping = (FunctionalMapping) obj;
		registerfunctionalMapping(functionalMapping);
		return functionalMapping;
	}

	// --------------------------------------------------------------------------------------------
	// 删除functionalMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 移除functionalMapping
	 * @param functionalMappingClassName
	 */
	static void removefunctionalMapping(String functionalMappingClassName) {
		if(FUNCTION_MAPPINGS != null) {
			FUNCTION_MAPPINGS.remove(functionalMappingClassName);
		}
	}
}
