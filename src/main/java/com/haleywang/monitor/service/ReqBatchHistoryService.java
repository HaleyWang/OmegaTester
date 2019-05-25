package com.haleywang.monitor.service;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.entity.ReqBatchHistory;

import java.util.List;

public interface ReqBatchHistoryService extends BaseService<ReqBatchHistory> {

	List<ReqBatchHistory> findByBatchId(Long batchId);


	List<ReqBatchHistory> findByBatchId(Long batchId, Sort sort);


}
