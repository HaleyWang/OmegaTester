package com.haleywang.monitor.service;

import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.entity.ReqTaskHistory;
import com.haleywang.monitor.entity.ReqTaskHistory.HisType;

import java.io.IOException;
import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqInfoService extends BaseService<ReqInfo> {

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param reqGroup
	 * @return
	 */
	public List<ReqInfo> listRequestInfoByReqGroup(ReqGroup reqGroup);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param reqGroupId
	 * @return
	 */
	public List<ReqInfo> listRequestInfoByReqGroup(Long reqGroupId);


	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param acc
	 * @return
	 */
	public List<ReqGroup> listRequestInfoByAccount(ReqAccount acc);


	/**
	 * Add req info
	 *
	 * @param ri
	 * @param by
	 * @return
	 */
	public ReqInfo add(ReqInfo ri, ReqAccount by);

	/**
	 * Update req info.
	 *
	 * @param ri
	 * @param by
	 * @return
	 */
	ReqInfo update(ReqInfo ri, ReqAccount by);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param id
	 * @param acc
	 * @return
	 */
	ReqInfo detail(Long id, ReqAccount acc);

	/**
	 * send request
	 *
	 * @param ri
	 * @param currentAccout
	 * @param batchHistoryId
	 * @param envStrring
	 * @return
	 * @throws IOException
	 */
	public UnirestRes send(ReqInfo ri, ReqAccount currentAccout, Long batchHistoryId, ReqSetting envStrring)
			throws IOException;

	/**
	 * send request
	 *
	 * @param ri
	 * @param currentAccout
	 * @return
	 * @throws IOException
	 */
	public UnirestRes send(ReqInfo ri, ReqAccount currentAccout) throws IOException;

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param currentAccout
	 * @param hisType
	 * @return
	 */
	List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout,
											HisType hisType);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param currentAccout
	 * @param batchHistoryId
	 * @return
	 */
	List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout,
											Long batchHistoryId);

	/**
	 * Retrieve a list of mapped object.
	 *
	 * @param currentAccout
	 * @param id
	 * @return
	 */
	ReqTaskHistory findHistoryDetail(ReqAccount currentAccout, Long id);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param currentAccout
	 * @param swaggerId
	 * @return
	 */
	List<ReqInfo> listRequestInfoBySwaggerId(ReqAccount currentAccout, String swaggerId);
}
