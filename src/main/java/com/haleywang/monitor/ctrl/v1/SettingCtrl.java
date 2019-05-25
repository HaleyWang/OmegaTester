package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.service.impl.ReqSettingServiceImpl;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by haley on 2018/8/18.
 */
public class SettingCtrl extends BaseCtrl {

    public ResultStatus<ReqSetting> save(@ParamBody ReqSetting ri) {

        ReqAccount acc = currentAccountAndCheck();
        return new ReqSettingServiceImpl().saveSetting(ri, acc);
    }

    public ResultStatus<ReqSetting> add(@ParamBody ReqSetting ri)  {

        ReqAccount acc = currentAccountAndCheck();
        ri.setOnwer(acc.getAccountId());

        return new ReqSettingServiceImpl().saveSetting(ri, acc);
    }

    public ResultStatus<ReqSetting> update(@ParamBody ReqSetting ri)  {
        return save(ri);
    }

    public ResultStatus<List<ReqSetting>> list()  {

        ReqSettingService reqSettingService = new ReqSettingServiceImpl();

        List<ReqSetting> ll = reqSettingService.findByOnwer(currentAccountAndCheck().getAccountId());
        return new ResultStatus<>(ll);
    }

    public ResultStatus<Long> delete()  {

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id, "Parameter id must be not null");

        ReqAccount acc = currentAccountAndCheck();
        return new ReqSettingServiceImpl().delete(id, acc);
    }

}
