package com.haleywang.monitor.service.impl;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.dao.ReqBatchHistoryRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;
import com.haleywang.monitor.entity.ReqBatchHistory;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqMeta;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
public class ReqJobService extends BaseServiceImpl<ReqBatch> {

    private static final int REQ_BATCH_HISTORY_MAX_SIZE_PER_BATCH_ID = 100;

    private ReqInfoService requestInfoService;
    private ReqBatchService reqBatchService;
    private ReqBatchHistoryService reqBatchHistoryService;
    private ReqBatchHistoryRepository reqBatchHistoryRepository;
    private ReqAccountService reqAccountService;

    public ReqJobService() {
        ReqBatchRepository requestInfoRepository = getMapper(ReqBatchRepository.class);
        this.reqBatchHistoryRepository = getMapper(ReqBatchHistoryRepository.class);
        this.mapper = (requestInfoRepository);

        this.requestInfoService = new ReqInfoServiceImpl();
        this.reqBatchService = new ReqBatchServiceImpl();
        this.reqBatchHistoryService = new ReqBatchHistoryServiceImpl();
        this.reqAccountService = new ReqAccountServiceImpl();
    }

    public void runBatch(Long batchId) {
        try {

            ReqBatch batch = reqBatchService.findOne(batchId);
            if (batch.getGroupId() == null) {
                return;
            }
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

    private void doRunBatch(ReqBatch batch)  {

        List<ReqInfo> ll = requestInfoService.listRequestInfoByReqGroup(batch.getBatchId());
        int total = ll.size();
        ReqBatchHistory.Statuts statuts = ReqBatchHistory.Statuts.PROCESSING;

        ReqBatchHistory reqBatchHistory = ReqBatchHistory.builder().batchId(batch.getBatchId())
                .total(total).batchStartDate(new Date()).build();
        reqBatchHistory = reqBatchHistoryService.save(reqBatchHistory);

        long t = System.currentTimeMillis();
        //split batch to tasks
        for (ReqInfo ri : ll) {
            ReqBatchHistory reqBatchHistoryDB = reqBatchHistoryService.findOne(reqBatchHistory);

            if (reqBatchHistoryDB.getStatuts() == ReqBatchHistory.Statuts.CANCELLED) {
                log.warn("Batch cancelled BatchHistoryId:{}" , reqBatchHistory.getBatchHistoryId());
                continue;
            }
            statuts = runOne(ri, reqBatchHistoryDB, batch, total);

        }
        reqBatchHistory.setCostTime(System.currentTimeMillis() - t);

        if (reqBatchHistory.getSuccessNum() == reqBatchHistory.getTotal()) {
            statuts = ReqBatchHistory.Statuts.COMPLETED;
        } else if(statuts != ReqBatchHistory.Statuts.CANCELLED) {
            statuts = ReqBatchHistory.Statuts.ERROR;
        }
        reqBatchHistory.setStatuts(statuts);

        reqBatchHistoryService.update(reqBatchHistory);

        removeOldReqBatchHistory(batch.getBatchId(), ReqBatchHistory.Statuts.COMPLETED);
        removeOldReqBatchHistory(batch.getBatchId(), ReqBatchHistory.Statuts.ERROR);
        removeOldReqBatchHistory(batch.getBatchId(), ReqBatchHistory.Statuts.CANCELLED);
    }

    private ReqBatchHistory.Statuts runOne(ReqInfo ri, ReqBatchHistory reqBatchHistoryDB, ReqBatch batch, int total){
        Long batchHistoryId = reqBatchHistoryDB.getBatchHistoryId();

        if(reqBatchHistoryDB.getTotal() == 0) {
            reqBatchHistoryDB.setTotal(total);
        }

        ReqAccount account = reqAccountService.findOne(batch.getCreatedById());
        ri = requestInfoService.detail(ri.getId(), account);
        UnirestRes res = null;
        try {
            res = requestInfoService.send(ri, account, batchHistoryId, null);
        } catch (IOException e) {
            return ReqBatchHistory.Statuts.PROCESSING;
        }
        if (res.getTestSuccess() == null || res.getTestSuccess()) {
            reqBatchHistoryDB.setSuccessNum(reqBatchHistoryDB.getSuccessNum() + 1);
        }

        reqBatchHistoryService.update(reqBatchHistoryDB);
        return ReqBatchHistory.Statuts.PROCESSING;
    }

    private void removeOldReqBatchHistory(Long batchId, ReqBatchHistory.Statuts statuts) {

        Example reqMetaExample = Example.builder(ReqBatchHistory.class).orderByDesc("batchHistoryId").build();
        reqMetaExample.createCriteria().andEqualTo("batchId", batchId)
                .andEqualTo("statuts", statuts.name());

        List<ReqBatchHistory> list = reqBatchHistoryRepository.selectByExample(reqMetaExample);

        for (int i = REQ_BATCH_HISTORY_MAX_SIZE_PER_BATCH_ID - 1, n = list.size(); i < n; i++) {
            reqBatchHistoryService.deleteByPrimaryKey(list.get(i).getBatchHistoryId());
        }
    }

}