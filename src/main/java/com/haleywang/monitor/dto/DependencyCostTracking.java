package com.haleywang.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public  class DependencyCostTracking {
    private String url;
    private Long cost;
    private Exception err;
    private String method;
    private Object requestData;
    private Map<String, String> requestHeader;
    private int status;
    private Map<String, String> responseHeader;
    private Object responseData;
    private Long begin;
 

    public DependencyCostTracking(long begin) {
    	this.setBegin(begin);
	}



	public void clearRequestDetails(){
        this.requestData = null;
        this.requestHeader = null;
        this.responseData = null;
        this.responseHeader = null;
    }

    public Map<String, String> getRequestHeader() {
        String apiKeyName = "ApiKey";
        int minaApiKeyLength = 4;
        if (requestHeader != null && requestHeader.get(apiKeyName) != null
                && requestHeader.get(apiKeyName).length() > minaApiKeyLength) {
            requestHeader.put(apiKeyName, requestHeader.get(apiKeyName).substring(0, 4) + "...");
        }
        return requestHeader;
    }

}