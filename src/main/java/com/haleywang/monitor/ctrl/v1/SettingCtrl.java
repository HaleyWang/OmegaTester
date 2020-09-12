package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.dto.msg.SettingDeleteMsg;
import com.haleywang.monitor.dto.msg.SettingSaveMsg;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.service.impl.ReqSettingServiceImpl;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author haley
 * @date 2018/12/16
 */
public class SettingCtrl extends BaseCtrl {

    public ResultMessage<ReqSetting, SettingSaveMsg> save(@ParamBody ReqSetting ri) {

        ReqAccount acc = currentAccountAndCheck();
        return new ReqSettingServiceImpl().saveSetting(ri, acc);
    }

    public ResultMessage<ReqSetting, SettingSaveMsg> add(@ParamBody ReqSetting ri)  {

        ReqAccount acc = currentAccountAndCheck();
        ri.setOnwer(acc.getAccountId());

        return new ReqSettingServiceImpl().saveSetting(ri, acc);
    }

    public ResultMessage<ReqSetting, SettingSaveMsg> update(@ParamBody ReqSetting ri)  {
        return save(ri);
    }

    public ResultStatus<List<ReqSetting>> list()  {

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();

        List<ReqSetting> ll = reqSettingService.findByOnwer(currentAccountAndCheck().getAccountId());
        return new ResultStatus<>(ll);
    }

    public ResultMessage<Long, SettingDeleteMsg> delete()  {

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id, "Parameter id must be not null");

        ReqAccount acc = currentAccountAndCheck();
        return new ReqSettingServiceImpl().delete(id, acc);
    }

}
