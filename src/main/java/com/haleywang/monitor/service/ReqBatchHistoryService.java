package com.haleywang.monitor.service;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.entity.ReqBatchHistory;

import java.util.List;
/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqBatchHistoryService extends BaseService<ReqBatchHistory> {

	/**
	 * Retrieve a list of mapped object.
	 *
	 * @param batchId
	 * @return
	 */
	List<ReqBatchHistory> findByBatchId(Long batchId);


	/**
	 * Retrieve a list of mapped object.
	 *
	 * @param batchId
	 * @param sort
	 * @return
	 */

	List<ReqBatchHistory> findByBatchId(Long batchId, Sort sort);


}
