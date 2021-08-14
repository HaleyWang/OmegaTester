package com.haleywang.monitor.service.impl;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.dao.ReqBatchHistoryRepository;
import com.haleywang.monitor.entity.ReqBatchHistory;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author haley
 */
public class ReqBatchHistoryServiceImpl extends BaseServiceImpl<ReqBatchHistory> implements ReqBatchHistoryService {

	private ReqBatchHistoryRepository reqBatchHistoryRepository;

	public ReqBatchHistoryServiceImpl() {
		initRepository();
	}

	private void initRepository() {
		this.reqBatchHistoryRepository = getMapper(ReqBatchHistoryRepository.class);
		this.mapper = (reqBatchHistoryRepository);
	}

	@Override
	public List<ReqBatchHistory> findByBatchId(Long batchId) {

		Example example1 = new Example(ReqBatchHistory.class);

		example1.createCriteria().andEqualTo("batchId", batchId);

		return reqBatchHistoryRepository.selectByExample(example1);
	}

	@Override
	public List<ReqBatchHistory> findByBatchId(Long batchId, Sort sort) {
		Example example1 = Example.builder(ReqBatchHistory.class).build();

		example1.createCriteria().andEqualTo("batchId", batchId);
		Example.OrderBy orderBy = example1.orderBy(sort.getField());
		if (sort.isAsc()) {
			orderBy.asc();
		} else {
			orderBy.desc();
		}
		return reqBatchHistoryRepository.selectByExample(example1);
	}

}
