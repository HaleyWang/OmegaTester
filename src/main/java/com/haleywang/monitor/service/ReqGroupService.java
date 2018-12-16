package com.haleywang.monitor.service;

import java.util.List;

import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqGroup;

public interface ReqGroupService extends BaseService<ReqGroup> {




	ReqGroup add(ReqGroup g);

	List<ReqGroup> listByAccount(ReqAccount currentAccout);



}
