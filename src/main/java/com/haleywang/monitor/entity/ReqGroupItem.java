package com.haleywang.monitor.entity;

import java.util.List;


/**
 * @author haley
 */
public interface ReqGroupItem {

	/**
	 * Returns id
	 *
	 * @return
	 */
	Long getId();

	/**
	 * Returns label
	 *
	 * @return
	 */
	String getLabel();

	/**
	 * Returns children
	 *
	 * @return
	 */
	List<ReqGroupItem> getChildren();

}
