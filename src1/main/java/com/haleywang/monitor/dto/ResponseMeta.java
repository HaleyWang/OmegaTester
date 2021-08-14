package com.haleywang.monitor.dto;

import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;
import lombok.Data;

/**
 * @author haley
 * @date 2018/12/16
 */
@Data
public class ResponseMeta {

    private String spentTime;
    private String logId = AppContext.getRequestId();
    private String serverName = ServerName.getLocalServerName();

}
