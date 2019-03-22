package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqBatch;

import java.util.List;

public interface ReqBatchRepository extends MyMapper<ReqBatch> {

    public ReqBatch findByName(String name);
    
    public List<ReqBatch> findByBatchIdIn(Long[] batchId);

}
