package com.haleywang.monitor.dto;

import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;
import lombok.Data;

@Data
public class ResponseMeta {
    public final static String SPENT_TIME_START = "spent_time_start";

    private String hint;
    private String response_status;
    private String spent_time;
    private String log_id = AppContext.getRequestId();
    private String serverName = ServerName.getLocalServerName();
    String code;
    String msg;

}
