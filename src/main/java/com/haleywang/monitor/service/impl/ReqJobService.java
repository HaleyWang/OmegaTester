package com.haleywang.monitor.service.impl;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.dao.*;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.model.*;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqInfoService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

public class ReqJobService extends BaseServiceImpl<ReqBatch> {

	static int REQBATCHHISTORY_MAX_SIZE_PER_BATCH_ID = 100;
	
	@Resource
	private ReqInfoService requestInfoService;
	
	@Resource
	ReqBatchService reqBatchService;
	
	@Resource
	private ReqBatchHistoryService reqBatchHistoryService;

	@Resource
	ReqBatchHistoryRepository reqBatchHistoryRepository;

	ReqAccountService reqAccountService;

	public ReqJobService() {
		ReqBatchRepository requestInfoRepository = getMapper(ReqBatchRepository.class);
		this.reqBatchHistoryRepository = getMapper(ReqBatchHistoryRepository.class);
		this.mapper = (requestInfoRepository);

		this.requestInfoService = new ReqInfoServiceImpl();
		this.reqBatchService = new ReqBatchServiceImpl();
		this.reqBatchHistoryService = new ReqBatchHistoryServiceImpl();
		this.reqAccountService = new ReqAccountServiceImpl();
	}



    private static final Logger LOG = LoggerFactory.getLogger(ReqJobService.class);

    public void hello() {
    	LOG.info("Hello World!");
    }
    
    public void execute() {
    	LOG.info("Hello World!");
    }


	public void runBatch( ReqBatch batch) throws MalformedURLException, UnirestException {

		ReqBatchHistory reqBatchHistory = new ReqBatchHistory();
		reqBatchHistory.setBatchId(batch.getBatchId());
		//TODO save batch history


		//split batch to tasks
		Long reqGroupId = batch.getGroupId();
		if(reqGroupId == null) {
			return;
		}
		List<ReqInfo> ll = requestInfoService.listRequestInfoByReqGroup(reqGroupId);
		ReqBatchHistory.Statuts statuts = ReqBatchHistory.Statuts.PROCESSING;

		reqBatchHistory.setTotal(ll.size());
		reqBatchHistory.setBatchStartDate(new Date());
		reqBatchHistory = reqBatchHistoryService.save(reqBatchHistory );

		long t = System.currentTimeMillis();
		for (ReqInfo ri : ll) {
			Long batchHistoryId = reqBatchHistory.getBatchHistoryId();
			ReqBatchHistory reqBatchHistoryDB = reqBatchHistoryService.findOne(reqBatchHistory);
			if(reqBatchHistoryDB == null ) {
				LOG.warn("ReqBatchHistory can not find batch by BatchHistoryId:" + batchHistoryId);
				break;
			}
			reqBatchHistory = reqBatchHistoryDB;
			if(reqBatchHistory.getStatuts() == ReqBatchHistory.Statuts.CANCELLED) {
				LOG.warn("Batch cancelled BatchHistoryId:" + batchHistoryId);
				statuts = ReqBatchHistory.Statuts.CANCELLED;
				break;
			}
			ReqAccount account = reqAccountService.findOne(batch.getCreatedById());
			ri = requestInfoService.detail(ri.getId(), account);
			UnirestRes<String> res = requestInfoService.send(ri, account,
					batchHistoryId, null);
			if(res.getTestSuccess() == null || res.getTestSuccess()) {
				reqBatchHistory.setSuccessNum(reqBatchHistory.getSuccessNum() + 1);
			}

			if(reqBatchHistory.getBatchHistoryId() ==null) {
				reqBatchHistoryService.save(reqBatchHistory );
			}else {
				reqBatchHistoryService.update(reqBatchHistory );
			}
		}
		reqBatchHistory.setCostTime(System.currentTimeMillis() -t);

		if(reqBatchHistory.getSuccessNum() == reqBatchHistory.getTotal()) {
			statuts = ReqBatchHistory.Statuts.COMPLETED;
		}else {
			statuts = ReqBatchHistory.Statuts.ERROR;
		}
		reqBatchHistory.setStatuts(statuts);

		reqBatchHistoryService.save(reqBatchHistory );

		//TODO
		//removeOldReqBatchHistory(scheduleJob.getBatchId(), ReqBatchHistory.Statuts.completed);
		//removeOldReqBatchHistory(scheduleJob.getBatchId(), ReqBatchHistory.Statuts.error);
	}

	private void removeOldReqBatchHistory( Long batchId, ReqBatchHistory.Statuts statuts)  {

		Sort sort =  Sort.of( "batchHistoryId", false);
		List<ReqBatchHistory> list = reqBatchHistoryRepository.findByBatchIdAndStatuts(batchId, statuts, sort);

		for(int i = REQBATCHHISTORY_MAX_SIZE_PER_BATCH_ID -1, n = list.size(); i < n ; i++ ) {
			//reqBatchHistoryService.delete(list.get(i).getBatchHistoryId());
		}
	}

}