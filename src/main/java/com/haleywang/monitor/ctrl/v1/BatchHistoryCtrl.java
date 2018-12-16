package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqBatchHistory;
import com.haleywang.monitor.mvc.BaseCtrl;
import com.haleywang.monitor.service.ReqBatchHistoryService;
import com.haleywang.monitor.service.impl.ReqBatchHistoryServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.List;

/**
 * Created by haley on 2018/8/18.
 */
public class BatchHistoryCtrl extends BaseCtrl {


    public String list() throws IOException, UnirestException {

        Long batchId = Long.parseLong(getUrlParam("batchId"));

        ResultStatus<List<ReqBatchHistory>> res = new ResultStatus<>();

        ReqBatchHistoryService service = new ReqBatchHistoryServiceImpl();


        List<ReqBatchHistory> ll = service.findByBatchId(batchId);

        res.setData(ll);

        return JsonUtils.toJson(res);
    }

}
