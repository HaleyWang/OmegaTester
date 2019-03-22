package com.haleywang.monitor.service;


import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;

public interface ReqBatchService extends BaseService<ReqBatch> {

    void update(ReqBatch reqBatch, ReqAccount reqAccount);

    ReqBatch save(ReqBatch model, ReqAccount reqAccount);

    int updateByVersion(ReqBatch model);

    void initDb();
}
