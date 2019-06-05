package com.douglei.def.mapping;

import java.util.List;

import com.douglei.sessionfactory.DynamicMapping;

/**
 * 默认映射
 * @author DougLei
 */
public interface DefaultMapping {
	
	String getId();
	
	/**
	 * 获取功能描述, 例如: 自动编码
	 * @return
	 */
	String getDescription();
	
	/**
	 * 获取映射集合
	 * @return
	 */
	List<DynamicMapping> getMappings();
}
