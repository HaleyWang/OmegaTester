package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqBatch;

import java.util.List;
/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqBatchRepository extends MyMapper<ReqBatch> {

    /**
     * Retrieve a list of mapped object.
     *
     * @param name
     * @return
     */
    public ReqBatch findByName(String name);

    /**
     * Retrieve a list of mapped objects.
     *
     * @param batchId
     * @return
     */
    public List<ReqBatch> findByBatchIdIn(Long[] batchId);

}
