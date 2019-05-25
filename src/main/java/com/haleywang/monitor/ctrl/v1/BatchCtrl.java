package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by haley on 2018/8/18.
 */
public class BatchCtrl extends BaseCtrl {

    public String add()  {
        ReqBatch reqBatch = getBodyParams(ReqBatch.class);

        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();
        service.save(reqBatch, currentAccountAndCheck());

        return JsonUtils.toJson(res.of(Msg.OK));
    }

    //
    public ResultStatus<List<ReqBatch>> update()  {
        ReqBatch reqBatch = getBodyParams(ReqBatch.class);

        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();
        ReqBatch reqBatchDb =  service.findOne(reqBatch.getBatchId());
        reqBatch.setCreatedById(reqBatchDb.getCreatedById());
        reqBatch.setModifiedById(currentAccountAndCheck().getAccountId());
        reqBatch.setUpdatedOn(new Date());

        service.update(reqBatch, currentAccount());

        return res.of(Msg.OK);
    }

    public String list()  {
        ResultStatus<List<ReqBatch>> res = new ResultStatus<>();

        ReqBatchService service = new ReqBatchServiceImpl();

        Example example = new Example(ReqBatch.class);
        example.orderBy("enable").desc();
        List<ReqBatch> ll = service.findAll(example);

        res.setData(ll);

        return JsonUtils.toJson(res);

    }

}
