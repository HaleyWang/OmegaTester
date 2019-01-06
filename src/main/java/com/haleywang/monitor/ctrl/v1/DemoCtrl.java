package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public class DemoCtrl extends BaseCtrl {

    public String version() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ResultStatus<String> res = new ResultStatus<>();
        return JsonUtils.toJson(res.of(Msg.OK, "1"));
    }

    public String comment() {

        String body = StringUtils.defaultString(getBodyParams(), "");

        ResultStatus<String> res = new ResultStatus<>();
        return JsonUtils.toJson(res.of(Msg.OK, "you input is "+ body));
    }




}
