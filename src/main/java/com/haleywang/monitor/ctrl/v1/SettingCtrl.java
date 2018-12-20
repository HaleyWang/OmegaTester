package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqSetting;
import com.haleywang.monitor.mvc.BaseCtrl;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.service.impl.ReqSettingServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by haley on 2018/8/18.
 */
public class SettingCtrl extends BaseCtrl {

    public String save() throws IOException {
        ReqSetting ri = getBodyParams(ReqSetting.class);

        ReqAccount acc = currentAccount();

        ResultStatus<ReqSetting> res = new ResultStatus<>();

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();

        if (ri.getId() != null) {
            ReqSetting ri1 = reqSettingService.findOne(ri.getId());
            if (ri1 == null) {
                return JsonUtils.toJson(res.of(Msg.NOT_FOUND));
            }
            //if (Objects.equals(ri1.getOnwer() , acc.getAccountId()) ) {
            //    return JsonUtils.toJson(res.of(Msg.NOT_ALLOWED));
            //}

            if (ri.getId() != null) {
                reqSettingService.update(ri);

            } else {
                reqSettingService.save(ri);
            }

            return JsonUtils.toJson(res);
        }

        ri.setOnwer(acc.getAccountId());
        reqSettingService.save(ri);

        return JsonUtils.toJson(res);
    }

    public String add() throws IOException {

        ReqSetting ri = getBodyParams(ReqSetting.class);

        ReqAccount acc = currentAccount();
        ri.setOnwer(acc.getAccountId());

        ResultStatus<ReqSetting> res = new ResultStatus<>();

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();

        if (ri.getId() != null) {
            ReqSetting ri1 = reqSettingService.findOne(ri.getId());
            if (ri1 == null) {
                return JsonUtils.toJson(res.of(Msg.NOT_FOUND));
            }
            if (Objects.equals(ri1.getOnwer() , acc.getAccountId())) {
                return JsonUtils.toJson(res.of(Msg.NOT_ALLOWED));
            }

            reqSettingService.save(ri);

            return JsonUtils.toJson(res);
        }

        ri.setOnwer(acc.getAccountId());
        reqSettingService.save(ri);

        return JsonUtils.toJson(res);
    }

    public String update() throws IOException {

        ReqSetting ri = getBodyParams(ReqSetting.class);

        ReqAccount acc = currentAccount();

        ResultStatus<ReqSetting> res = new ResultStatus<>();

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();
        ReqSetting ri1 = reqSettingService.findOne(ri.getId());
        if (ri1 == null) {
            return JsonUtils.toJson(res.of(Msg.NOT_FOUND));
        }

        if (Objects.equals(ri1.getOnwer() , acc.getAccountId())) {
            return JsonUtils.toJson(res.of(Msg.NOT_ALLOWED));
        }

        ri.setOnwer(acc.getAccountId());
        reqSettingService.save(ri);

        return JsonUtils.toJson(res.of(Msg.OK));

    }

    public String list() throws IOException {
        System.out.println(" ====> list");



        ResultStatus<List<ReqSetting>> res = new ResultStatus<>();

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();

        List<ReqSetting> ll = reqSettingService.findByOnwer(currentAccount().getAccountId());

        res.setData(ll);

        return JsonUtils.toJson(res.of(Msg.OK));

    }

    public String delete() throws IOException {

        System.out.println(" ====> delete");

        Long id = Long.parseLong(getUrlParam("id"));

        ReqAccount acc = currentAccount();

        ResultStatus<List<ReqSetting>> res = new ResultStatus<>();

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();
        ReqSetting ri1 = reqSettingService.findOne(id);
        if (ri1 == null) {
            return JsonUtils.toJson(res.of(Msg.NOT_FOUND));
        }

        if (Objects.equals(ri1.getOnwer() , acc.getAccountId())) {
            return JsonUtils.toJson(res.of(Msg.NOT_ALLOWED));
        }

        reqSettingService.delete(ri1);

        return JsonUtils.toJson(res.of(Msg.OK));
    }

}
