package com.douglei.func.mapping;

import java.util.ArrayList;
import java.util.List;

import com.douglei.sessionfactory.DynamicMapping;

/**
 * 功能映射
 * @author DougLei
 */
public abstract class FunctionMapping {
	protected List<DynamicMapping> mappings;
	private List<String> mappingCodes;
	
	/**
	 * 获取功能描述, 例如: 自动编码
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * 注册需要的映射
	 */
	protected abstract void registerMappings();
	
	/**
	 * 获取功能映射集合
	 * @return
	 */
	public List<DynamicMapping> getMappings(){
		if(mappings == null) {
			registerMappings();
		}
		return mappings;
	}

	/**
	 * 获取功能映射的code集合
	 * @return
	 */
	public List<String> getMappingCodes() {
		if(mappingCodes == null) {
			List<DynamicMapping> dms = getMappings();
			mappingCodes = new ArrayList<String>(dms.size());
			for (DynamicMapping dm : dms) {
				mappingCodes.add(dm.getMappingCode());
			}
		}
		return mappingCodes;
	}
}
