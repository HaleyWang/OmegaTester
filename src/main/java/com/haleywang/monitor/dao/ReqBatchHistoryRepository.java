package com.haleywang.monitor.dao;

import java.util.List;

import com.haleywang.db.mapper.MyMapper;

import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.entity.ReqBatchHistory;

public interface ReqBatchHistoryRepository extends MyMapper<ReqBatchHistory> {

    public List<ReqBatchHistory> findByBatchId(Long batchId);

    public List<ReqBatchHistory> findByBatchId(Long batchId, Sort sort);

    public List<ReqBatchHistory> findByBatchIdAndStatuts(Long batchId, ReqBatchHistory.Statuts Statuts, Sort sort);


}
