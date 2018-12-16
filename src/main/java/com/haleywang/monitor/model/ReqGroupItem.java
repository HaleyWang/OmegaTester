package com.haleywang.monitor.model;

import java.util.List;

public interface ReqGroupItem {

	Long getId();
	String getLabel();
	
	List<ReqGroupItem> getChildren();
	
}
