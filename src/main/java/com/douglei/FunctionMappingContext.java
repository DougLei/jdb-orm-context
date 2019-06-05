package com.douglei;

import java.util.HashMap;
import java.util.Map;

import com.douglei.exception.NotExistsFunctionMappingClassException;
import com.douglei.exception.UnsupportFunctionMappingClassException;
import com.douglei.func.mapping.FunctionMapping;
import com.douglei.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
class FunctionMappingContext {
	private static Map<String, FunctionMapping> FUNCTION_MAPPINGS;// 功能映射集合
	
	// --------------------------------------------------------------------------------------------
	// 注册FunctionMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 注册FunctionMapping
	 * @param functionMapping
	 */
	static void registerFunctionMapping(FunctionMapping functionMapping) {
		String functionMappingClassName = functionMapping.getClass().getName();
		if(FUNCTION_MAPPINGS == null) {
			FUNCTION_MAPPINGS = new HashMap<String, FunctionMapping>(10);
		}else if(FUNCTION_MAPPINGS.containsKey(functionMappingClassName)) {
			return;
		}
		FUNCTION_MAPPINGS.put(functionMappingClassName, functionMapping);
	}

	// --------------------------------------------------------------------------------------------
	// 获取FunctionMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 根据FunctionMapping ClassName获取对应的实例
	 * @param functionMappingClassName
	 * @return
	 */
	static FunctionMapping getFunctionMapping(String functionMappingClassName) {
		if(FUNCTION_MAPPINGS == null) {
			FUNCTION_MAPPINGS = new HashMap<String, FunctionMapping>(10);
		}else if(FUNCTION_MAPPINGS.containsKey(functionMappingClassName)) {
			return FUNCTION_MAPPINGS.get(functionMappingClassName);
		}
		
		Object obj = null;
		try {
			obj = ConstructorUtil.newInstance(functionMappingClassName);
		} catch (Exception e) {
			throw new NotExistsFunctionMappingClassException(functionMappingClassName, e);
		}
		if(!(obj instanceof FunctionMapping)) {
			throw new UnsupportFunctionMappingClassException("["+functionMappingClassName+"]类必须继承["+FunctionMapping.class.getName()+"]");
		}
		FunctionMapping functionMapping = (FunctionMapping) obj;
		registerFunctionMapping(functionMapping);
		return functionMapping;
	}

	// --------------------------------------------------------------------------------------------
	// 删除FunctionMapping
	// --------------------------------------------------------------------------------------------
	/**
	 * 移除FunctionMapping
	 * @param functionMappingClassName
	 */
	static void removeFunctionMapping(String functionMappingClassName) {
		if(FUNCTION_MAPPINGS != null) {
			FUNCTION_MAPPINGS.remove(functionMappingClassName);
		}
	}
}
