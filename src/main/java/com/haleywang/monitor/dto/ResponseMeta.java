package com.haleywang.monitor.dto;

import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;
import lombok.Data;

@Data
public class ResponseMeta {
    public static final  String SPENT_TIME_START = "spent_time_start";

    private String hint;
    private String responseStatus;
    private String spentTime;
    private String logId = AppContext.getRequestId();
    private String serverName = ServerName.getLocalServerName();
    private String code;
    private String msg;

}
