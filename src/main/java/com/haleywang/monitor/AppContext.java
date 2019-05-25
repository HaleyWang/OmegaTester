package com.haleywang.monitor;

import com.haleywang.monitor.common.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppContext {

	public static final String VERSION = "0.1";

    public static final String DEBUG_REQUEST = "debug_request";
    
    
    public static String getRequestId(){
        return MDC.get(Constants.REQUEST_ID_IN_LOG);
    }
    public static void setRequestId(String requestId){
        MDC.put(Constants.REQUEST_ID_IN_LOG, requestId);
    }
	public static Object getVersion() {
		return VERSION;
	}
	public static void setAccountId(Long accid) {
		MDC.put(Constants.CURRENT_ACCOUNT, accid +"");
	}
	public static String getAccountId() {
		return MDC.get(Constants.CURRENT_ACCOUNT);
	}
    
}
