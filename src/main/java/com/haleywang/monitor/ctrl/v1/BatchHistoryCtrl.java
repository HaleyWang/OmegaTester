package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqBatchHistory;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import com.haleywang.monitor.service.impl.ReqBatchHistoryServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;

import java.util.List;

/**
 * Created by haley on 2018/8/18.
 */
public class BatchHistoryCtrl extends BaseCtrl {


    public ResultStatus<List<ReqBatchHistory>> list()  {

        Long batchId = Long.parseLong(getUrlParam("batchId"));

        ReqBatchHistoryService service = new ReqBatchHistoryServiceImpl();

        List<ReqBatchHistory> ll = service.findByBatchId(batchId);

        return new ResultStatus<>(ll);
    }

    public String  detail()  {
        Long batchHistoryId = Long.parseLong(getUrlParam("batchHistoryId"));

        ReqBatchHistoryService reqBatchHistoryService = new ReqBatchHistoryServiceImpl();

        ReqBatchHistory o = reqBatchHistoryService.findOne(batchHistoryId);

        return JsonUtils.toJson(o);
    }


    public ResultStatus<Long> delete() {
        Long batchHistoryId = Long.parseLong(getUrlParam("batchHistoryId"));

        ReqBatchHistoryService reqBatchHistoryService = new ReqBatchHistoryServiceImpl();

        reqBatchHistoryService.deleteByPrimaryKey(batchHistoryId);
        return new ResultStatus<>(batchHistoryId);
    }

}
