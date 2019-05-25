package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.dto.ConfigDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.service.impl.ReqSettingServiceImpl;


public class ConfigCtrl extends BaseCtrl {

    public ResultStatus<ConfigDto> list() {

        ConfigDto dto = new ReqSettingServiceImpl().parseConfigDto();
        dto.setMethods(HttpMethod.values());
        return new ResultStatus<>(dto);
    }

    public ResultStatus<HttpMethod[]> methods() {
        return new ResultStatus<>(HttpMethod.values());
    }
}
