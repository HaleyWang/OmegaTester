package com.haleywang.monitor.service;


import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqBatch;

import java.util.List;

public interface ReqBatchService extends BaseService<ReqBatch> {

     void update(ReqBatch reqBatch, ReqAccount reqAccount);
     ReqBatch save(ReqBatch model, ReqAccount reqAccount) ;


    void initDb();
}
