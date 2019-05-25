package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.dto.ResultStatus;
import org.apache.commons.lang3.StringUtils;

public class DemoCtrl extends BaseCtrl {

    public ResultStatus<String> version() {
        return new ResultStatus<>("1");
    }

    public ResultStatus<String> comment() {

        String body = StringUtils.defaultString(getBodyParams(), StringUtils.EMPTY);

        return new ResultStatus<>( "you input is "+ body);
    }

}
