package com.haleywang.monitor;

import java.util.Set;

import org.slf4j.MDC;

import com.haleywang.monitor.common.Constants;

public class AppContext {

    public static final String DEBUG_REQUEST = "debug_request";
    
    
    public static String getRequestId(){
        return MDC.get(Constants.REQUEST_ID_IN_LOG);
    }
    public static void setRequestId(String requestId){
        MDC.put(Constants.REQUEST_ID_IN_LOG, requestId);
    }
	public static Object getVersion() {
		return "0.1";
	}
	public static void setAccountId(Long accid) {
		MDC.put(Constants.CURRENT_ACCOUNT, accid +"");
	}
	public static String getAccountId() {
		return MDC.get(Constants.CURRENT_ACCOUNT);
	}
    
}
