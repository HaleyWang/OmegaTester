package com.haleywang.monitor.entity;

import java.util.List;

public interface ReqGroupItem {

	Long getId();
	String getLabel();
	
	List<ReqGroupItem> getChildren();
	
}
