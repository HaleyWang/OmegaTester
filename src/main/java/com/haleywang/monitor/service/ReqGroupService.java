package com.haleywang.monitor.service;

import java.util.List;

import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;

public interface ReqGroupService extends BaseService<ReqGroup> {




	ReqGroup add(ReqGroup g);

	List<ReqGroup> listByAccount(ReqAccount currentAccout);



}
