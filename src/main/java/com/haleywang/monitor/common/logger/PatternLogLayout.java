package com.haleywang.monitor.common.logger;

import java.util.Map;

import ch.qos.logback.classic.PatternLayout;

public class PatternLogLayout extends PatternLayout {
	public PatternLogLayout(){
		super();
	}
	
	@Override
    public Map<String, String> getDefaultConverterMap(){
		defaultConverterMap.put("m", CustomMessageConverter.class.getName());
		return defaultConverterMap;
    }
}