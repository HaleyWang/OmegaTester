package com.haleywang.monitor.service.impl;

import com.haleywang.db.DBUtils;
import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.dao.*;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.*;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqInfoService;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
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


    public void runBatch(Long batchId) throws MalformedURLException {
        try {

            ReqBatch batch = reqBatchService.findOne(batchId);
            if (!BooleanUtils.isTrue(batch.getEnable())) {
                return;
            }
            //TODO check wait timeout
            if (ReqBatch.Status.RUNNING.name().equals(batch.getStatus())) {
                return;
            }


            batch.setStatus(ReqBatch.Status.RUNNING.name());
            int num = reqBatchService.updateByVersion(batch);
            if (num <= 0) {
                return;
            }


            try {
                doRunBatch(batch);
            } finally {
                ReqBatch batchObj = reqBatchService.findOne(batchId);
                batchObj.setStatus("");
                reqBatchService.update(batchObj);
            }
        } finally {
            DBUtils.closeSession(true);
        }
    }

    private void doRunBatch(ReqBatch batch) throws MalformedURLException {


        ReqBatchHistory reqBatchHistory = new ReqBatchHistory();
        reqBatchHistory.setBatchId(batch.getBatchId());
        //TODO save batch history


        //split batch to tasks
        Long reqGroupId = batch.getGroupId();
        if (reqGroupId == null) {
            return;
        }
        List<ReqInfo> ll = requestInfoService.listRequestInfoByReqGroup(reqGroupId);
        ReqBatchHistory.Statuts statuts = ReqBatchHistory.Statuts.PROCESSING;

        reqBatchHistory.setTotal(ll.size());
        reqBatchHistory.setBatchStartDate(new Date());
        reqBatchHistory = reqBatchHistoryService.save(reqBatchHistory);

        long t = System.currentTimeMillis();
        for (ReqInfo ri : ll) {
            Long batchHistoryId = reqBatchHistory.getBatchHistoryId();
            ReqBatchHistory reqBatchHistoryDB = reqBatchHistoryService.findOne(reqBatchHistory);
            if (reqBatchHistoryDB == null) {
                LOG.warn("ReqBatchHistory can not find batch by BatchHistoryId:" + batchHistoryId);
                break;
            }
            reqBatchHistory = reqBatchHistoryDB;
            if(reqBatchHistory.getTotal() == 0) {
                reqBatchHistory.setTotal(ll.size());
            }
            if (reqBatchHistory.getStatuts() == ReqBatchHistory.Statuts.CANCELLED) {
                LOG.warn("Batch cancelled BatchHistoryId:" + batchHistoryId);
                statuts = ReqBatchHistory.Statuts.CANCELLED;
                break;
            }
            ReqAccount account = reqAccountService.findOne(batch.getCreatedById());
            ri = requestInfoService.detail(ri.getId(), account);
            UnirestRes res = null;
            try {
                res = requestInfoService.send(ri, account,
                        batchHistoryId, null);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (res.getTestSuccess() == null || res.getTestSuccess()) {
                reqBatchHistory.setSuccessNum(reqBatchHistory.getSuccessNum() + 1);
            }

            if (reqBatchHistory.getBatchHistoryId() == null) {
                reqBatchHistoryService.save(reqBatchHistory);
            } else {
                reqBatchHistoryService.update(reqBatchHistory);
            }
        }
        reqBatchHistory.setCostTime(System.currentTimeMillis() - t);

        if (reqBatchHistory.getSuccessNum() == reqBatchHistory.getTotal()) {
            statuts = ReqBatchHistory.Statuts.COMPLETED;
        } else {
            statuts = ReqBatchHistory.Statuts.ERROR;
        }
        reqBatchHistory.setStatuts(statuts);

        if (reqBatchHistory.getBatchHistoryId() != null) {
            reqBatchHistoryService.update(reqBatchHistory);
        } else {
            reqBatchHistoryService.save(reqBatchHistory);
        }

        //TODO
        //removeOldReqBatchHistory(scheduleJob.getBatchId(), ReqBatchHistory.Statuts.completed);
        //removeOldReqBatchHistory(scheduleJob.getBatchId(), ReqBatchHistory.Statuts.error);
    }

    private void removeOldReqBatchHistory(Long batchId, ReqBatchHistory.Statuts statuts) {

        Sort sort = Sort.of("batchHistoryId", false);
        List<ReqBatchHistory> list = reqBatchHistoryRepository.findByBatchIdAndStatuts(batchId, statuts, sort);

        for (int i = REQBATCHHISTORY_MAX_SIZE_PER_BATCH_ID - 1, n = list.size(); i < n; i++) {
            //reqBatchHistoryService.delete(list.get(i).getBatchHistoryId());
        }
    }

}