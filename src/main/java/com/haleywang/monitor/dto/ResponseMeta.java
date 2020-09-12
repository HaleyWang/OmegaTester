package com.haleywang.monitor.dto;

import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;
import lombok.Data;

@Data
public class ResponseMeta {

    private String spentTime;
    private String logId = AppContext.getRequestId();
    private String serverName = ServerName.getLocalServerName();

}
