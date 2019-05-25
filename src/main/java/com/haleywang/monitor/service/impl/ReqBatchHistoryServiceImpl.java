package com.haleywang.monitor.service.impl;

import java.util.List;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.dao.ReqBatchHistoryRepository;
import com.haleywang.monitor.entity.ReqBatchHistory;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import tk.mybatis.mapper.entity.Example;

public class ReqBatchHistoryServiceImpl extends BaseServiceImpl<ReqBatchHistory> implements ReqBatchHistoryService {

	private ReqBatchHistoryRepository reqBatchHistoryRepository;

	public ReqBatchHistoryServiceImpl() {
		initRepository();
	}

	private void initRepository() {
		this.reqBatchHistoryRepository = getMapper(ReqBatchHistoryRepository.class);
		this.mapper = (reqBatchHistoryRepository);
	}
	
	
	public List<ReqBatchHistory> findByBatchId(Long batchId) {

		Example example1 = new Example(ReqBatchHistory.class);

		example1.createCriteria().andEqualTo("batchId", batchId);

		return reqBatchHistoryRepository.selectByExample(example1);
	}


	public List<ReqBatchHistory> findByBatchId(Long batchId, Sort sort) {
		return reqBatchHistoryRepository.findByBatchId(batchId, sort);
	}


}
