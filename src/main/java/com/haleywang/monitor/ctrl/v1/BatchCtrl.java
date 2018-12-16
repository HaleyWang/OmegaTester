package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.mvc.BaseCtrl;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.List;

/**
 * Created by haley on 2018/8/18.
 */
public class BatchCtrl extends BaseCtrl {

    public String add() throws IOException, UnirestException {
        ReqBatch reqBatch = getBodyParams(ReqBatch.class);

        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();
        service.save(reqBatch);

        return JsonUtils.toJson(res.of(Msg.OK));
    }

    //
    public String update() throws IOException, UnirestException {
        ReqBatch reqBatch = getBodyParams(ReqBatch.class);

        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();
        ReqBatch reqBatchDb =  service.findOne(reqBatch.getBatchId());
        reqBatchDb.setEnable(reqBatch.getEnable());
        service.update(reqBatchDb);

        return JsonUtils.toJson(res.of(Msg.OK));
    }

    public String list() throws IOException, UnirestException {
        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();

        List<ReqBatch> ll = service.findAll();

        res.setData(ll);

        return JsonUtils.toJson(res);

    }

}
