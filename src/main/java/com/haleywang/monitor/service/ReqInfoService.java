package com.haleywang.monitor.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqGroup;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.model.ReqSetting;
import com.haleywang.monitor.model.ReqTaskHistory;
import com.haleywang.monitor.model.ReqTaskHistory.HisType;

public interface ReqInfoService extends BaseService<ReqInfo> {

	public List<ReqInfo> listRequestInfoByReqGroup( ReqGroup reqGroup);
	public List<ReqInfo> listRequestInfoByReqGroup( Long reqGroupId);


	public List<ReqGroup> listRequestInfoByAccount(ReqAccount acc);

	public ReqInfo add(ReqInfo ri, ReqAccount by) ;

	ReqInfo update(ReqInfo ri, ReqAccount by);

	ReqInfo detail(Long id, ReqAccount acc);

	public UnirestRes send(ReqInfo ri, ReqAccount currentAccout, Long batchHistoryId, ReqSetting envStrring)
			throws IOException;

	public UnirestRes send(ReqInfo ri, ReqAccount currentAccout) throws IOException;

	List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout,
			HisType hisType);
	
	List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout,
			Long batchHistoryId);

	ReqTaskHistory findHistoryDetail(ReqAccount currentAccout, Long id);

    List<ReqInfo> listRequestInfoBySwaggerId(ReqAccount currentAccout, String swaggerId);
}
