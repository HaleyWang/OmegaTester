package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.common.req.converter.ConverterBuilder;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.dto.msg.GroupDeleteMsg;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.impl.ReqGroupServiceImpl;
import com.haleywang.monitor.service.impl.ReqInfoServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.ReqFormatter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ReqCtrl extends BaseCtrl {


    public String format() {
        String body = StringUtils.defaultString(getBodyParams(), StringUtils.EMPTY).trim();
        return new ReqFormatter().format(body);
    }

    public ResultStatus<String> version() {
        return new ResultStatus<>(getClass().getPackage().getImplementationVersion());
    }

    public ResultStatus<ReqInfo> add(@ParamBody ReqInfo ri)  {
        ReqAccount acc = currentAccountAndCheck();


        new ReqInfoServiceImpl().add(ri, acc);
        return new ResultStatus<>(ri);
    }

    public ResultStatus<ReqInfo> update(@ParamBody ReqInfo ri) {

        checkNotNull(ri);
        ReqAccount acc = currentAccountAndCheck();

        new ReqInfoServiceImpl().update(ri, acc);

        return new ResultStatus<>(ri);
    }

    public ResultStatus<List<String>> importType() {
        return new ResultStatus<>(Constants.IMPORT_TYPE);
    }

    public ResultStatus<List<String>> exportType() {
        return new ResultStatus<>(Constants.EXPORT_TYPE);
    }

    public ResultStatus<String> importRequest(@ParamBody TypeValuePair ri) {
        MyRequest data = new ConverterBuilder().build(ri.getType()).toMyRequest(ri);

        return new ResultStatus<>(JsonUtils.toJson(data));
    }

    public ResultStatus<String> exportRequest(@ParamBody TypeValuePair ri) {

        String data = new ConverterBuilder().build(ri.getType())
                .fromMyRequest(ri);

        return new ResultStatus<>(data);
    }

    public ResultStatus<UnirestRes> send(@ParamBody ReqInfo ri) throws IOException {

        UnirestRes result = new ReqInfoServiceImpl().send(ri, currentAccountAndCheck());

        return new ResultStatus<>().withData(result);
    }

    public ResultStatus<List<ReqGroup>> list() {

        ReqAccount acc = currentAccountAndCheck();

        List<ReqGroup> ll = new ReqInfoServiceImpl().listRequestInfoByAccount(acc);

        return new ResultStatus<>().withData(ll);
    }

    public ResultStatus<List<ReqGroup>> tree() {
        return list();
    }

    public ResultStatus<List<ReqInfo>> listBySwaggerId() {

        String swaggerId = getUrlParam("swaggerId");
        checkNotNull(swaggerId);

        List<ReqInfo> ll = new ReqInfoServiceImpl().listRequestInfoBySwaggerId(currentAccountAndCheck(), swaggerId);

        return new ResultStatus<>().withData(ll);
    }

    public ResultStatus<ReqInfo> detail() {

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id, "Parameter id must be not null");

        ReqAccount acc = currentAccountAndCheck();

        ReqInfo ri = new ReqInfoServiceImpl().detail(id, acc);
        return new ResultStatus<>(ri);
    }

    public ResultStatus<Long> delete() {

        Long id = Long.parseLong(getUrlParam("id"));
        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        requestInfoService.deleteByPrimaryKey(id);
        return new ResultStatus<>(id);
    }

    public ResultStatus<List<ReqGroup>> groupList() {

        ReqAccount acc = currentAccountAndCheck();

        List<ReqGroup> ll = new ReqGroupServiceImpl().listByAccount(acc);
        return new ResultStatus<>(ll);
    }

    public ResultStatus<ReqGroup> groupAdd(@ParamBody  ReqGroup g) {

        checkNotNull(g);
        new ReqGroupServiceImpl().add(g);
        return new ResultStatus<>(g);
    }

    public ResultStatus<ReqGroup> groupUpdate(@ParamBody  ReqGroup g) {

        checkNotNull(g);

        ReqGroupService reqGroupService = new ReqGroupServiceImpl();
        ReqGroup reqGroup = reqGroupService.findOne(g.getGroupId());
        reqGroup.setName(g.getName());
        reqGroupService.update(reqGroup);

        return new ResultStatus<>(reqGroup);
    }

    public ResultMessage<ReqGroup, GroupDeleteMsg> groupDelete() {
        ReqAccount acc = currentAccountAndCheck();

        Long id = Long.parseLong(getUrlParam("id"));
        return new ReqGroupServiceImpl().groupDelete(id, acc);
    }

}
