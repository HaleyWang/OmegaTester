package com.haleywang.monitor.service.impl;

import com.haleywang.db.DbUtils;
import com.haleywang.monitor.dao.ReqBatchHistoryRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;
import com.haleywang.monitor.entity.ReqBatchHistory;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.Status;
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
import java.util.Objects;

/**
 * @author haley
 * @date 2018/12/16
 */
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
            DbUtils.closeSession(true);
        }
    }

    private void doRunBatch(ReqBatch batch)  {

        List<ReqInfo> ll = requestInfoService.listRequestInfoByReqGroup(batch.getGroupId());
        int total = ll.size();
        Status status = Status.PROCESSING;

        ReqBatchHistory reqBatchHistory = ReqBatchHistory.builder().batchId(batch.getBatchId())
                .total(total).batchStartDate(new Date()).build();
        reqBatchHistory = reqBatchHistoryService.save(reqBatchHistory);

        if(reqBatchHistory == null) {
            return;
        }

        long t = System.currentTimeMillis();
        //split batch to tasks
        for (ReqInfo ri : ll) {
            reqBatchHistory = reqBatchHistoryService.findOne(reqBatchHistory.getBatchHistoryId());

            if (reqBatchHistory == null || reqBatchHistory.getStatus() == Status.CANCELLED) {
                log.warn("Batch cancelled BatchHistoryId:{}", reqBatchHistory.getBatchHistoryId());
                continue;
            }
            status = runOne(ri, reqBatchHistory, batch, total);

        }
        reqBatchHistory.setCostTime(System.currentTimeMillis() - t);

        if (Objects.equals(reqBatchHistory.getSuccessNum(), reqBatchHistory.getTotal())) {
            status = Status.COMPLETED;
        } else if (status != Status.CANCELLED) {
            status = Status.ERROR;
        }
        reqBatchHistory.setStatus(status);

        reqBatchHistoryService.update(reqBatchHistory);

        removeOldReqBatchHistory(batch.getBatchId());
    }

    private Status runOne(ReqInfo ri, ReqBatchHistory reqBatchHistoryDb, ReqBatch batch, int total) {
        Long batchHistoryId = reqBatchHistoryDb.getBatchHistoryId();

        if (reqBatchHistoryDb.getTotal() == 0) {
            reqBatchHistoryDb.setTotal(total);
        }

        ReqAccount account = reqAccountService.findOne(batch.getCreatedById());
        ri = requestInfoService.detail(ri.getId(), account);
        UnirestRes res = null;
        try {
            res = requestInfoService.send(ri, account, batchHistoryId, null);
        } catch (IOException e) {
            return Status.PROCESSING;
        }
        if (res.getTestSuccess() == null || res.getTestSuccess()) {
            reqBatchHistoryDb.setSuccessNum(reqBatchHistoryDb.getSuccessNum() + 1);
        }

        reqBatchHistoryService.update(reqBatchHistoryDb);
        return Status.PROCESSING;
    }

    private void removeOldReqBatchHistory(Long batchId) {

        for (Status value : Status.values()) {
            removeOldReqBatchHistory(batchId, value);
        }
    }


    private void removeOldReqBatchHistory(Long batchId, Status status) {

        Example reqMetaExample = Example.builder(ReqBatchHistory.class).orderByDesc("batchHistoryId").build();
        reqMetaExample.createCriteria().andEqualTo("batchId", batchId)
                .andEqualTo("status", status.name());

        List<ReqBatchHistory> list = reqBatchHistoryRepository.selectByExample(reqMetaExample);

        for (int i = REQ_BATCH_HISTORY_MAX_SIZE_PER_BATCH_ID - 1, n = list.size(); i < n; i++) {
            reqBatchHistoryService.deleteByPrimaryKey(list.get(i).getBatchHistoryId());
        }
    }

}