package com.haleywang.monitor.dto;

import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.utils.ServerName;

public class ResponseMeta {
    private String hint;
    private Object data;
    private String response_status;
    private String spent_time;
    private String log_id = AppContext.getRequestId();
    private String serverName = ServerName.getLocalServerName();
    String code;
    String msg;
    
    public final static String SPENT_TIME_START = "spent_time_start";

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public String getSpent_time() {
        return spent_time;
    }

    public void setSpent_time(String spent_time) {
        this.spent_time = spent_time;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
