package com.haleywang.monitor.service;


import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;
/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqBatchService extends BaseService<ReqBatch> {

    /**
     * Update req batch
     *
     * @param reqBatch
     * @param reqAccount
     */
    void update(ReqBatch reqBatch, ReqAccount reqAccount);

    /**
     * Save req batch
     *
     * @param model
     * @param reqAccount
     * @return
     */
    ReqBatch save(ReqBatch model, ReqAccount reqAccount);

    /**
     * Update by version
     *
     * @param model
     * @return
     */
    int updateByVersion(ReqBatch model);

    /**
     * Intit db
     *
     * @param dropTableBefore
     */
    void initDb(boolean dropTableBefore);
}
