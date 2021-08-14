package com.haleywang.monitor.service;

import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;

import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqGroupService extends BaseService<ReqGroup> {


	/**
	 * Add req group
	 *
	 * @param g
	 * @return
	 */
	ReqGroup add(ReqGroup g);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param currentAccout
	 * @return
	 */
	List<ReqGroup> listByAccount(ReqAccount currentAccout);


}
