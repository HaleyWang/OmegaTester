package com.haleywang.monitor.common.logger;

import ch.qos.logback.classic.PatternLayout;

import java.util.Map;

/**
 * @author haley
 * @date 2018/12/16
 */
public class PatternLogLayout extends PatternLayout {
	public PatternLogLayout() {
		super();
	}

	@Override
	public Map<String, String> getDefaultConverterMap() {
		defaultConverterMap.put("m", CustomMessageConverter.class.getName());
		return defaultConverterMap;
	}
}