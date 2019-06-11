package com.douglei.orm.context.necessary.mapping.configuration;

import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.sessionfactory.DynamicMapping;

/**
 * 必须的映射配置
 * @author DougLei
 */
public abstract class NecessaryMappingConfiguration {
	protected List<DynamicMapping> mappings;
	private List<String> mappingCodes;
	
	/**
	 * 获取功能描述, 例如: 自动编码
	 * @return
	 */
	public abstract String getFunctionDescription();
	
	/**
	 * 注册必须的映射
	 * 注册到super.mappings集合属性中
	 */
	protected abstract void registerNecessaryMappings();
	
	/**
	 * 获取必须的映射集合
	 * @return
	 */
	public List<DynamicMapping> getNecessaryMappings(){
		if(mappings == null) {
			registerNecessaryMappings();
		}
		return mappings;
	}

	/**
	 * 获取必须映射的code集合
	 * @return
	 */
	public List<String> getMappingCodes() {
		if(mappingCodes == null) {
			List<DynamicMapping> dms = getNecessaryMappings();
			mappingCodes = new ArrayList<String>(dms.size());
			for (DynamicMapping dm : dms) {
				mappingCodes.add(dm.getMappingCode());
			}
		}
		return mappingCodes;
	}
}
