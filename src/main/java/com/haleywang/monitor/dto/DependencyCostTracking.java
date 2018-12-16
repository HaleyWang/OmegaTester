package com.haleywang.monitor.dto;

import java.util.Map;

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



    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url = url;
    }



    public Long getCost() {
        return cost;
    }



    public void setCost(Long cost) {
        this.cost = cost;
    }



    public Exception getErr() {
        return err;
    }



    public void setErr(Exception err) {
        this.err = err;
    }



    public String getMethod() {
        return method;
    }



    public void setMethod(String method) {
        this.method = method;
    }



    public Object getRequestData() {
        return requestData;
    }



    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }



    public Map<String, String> getRequestHeader() {
    	String apiKeyName  = "ApiKey";
    	if(requestHeader != null && requestHeader.get(apiKeyName) != null && requestHeader.get(apiKeyName).length() > 4) {
    		requestHeader.put(apiKeyName, requestHeader.get(apiKeyName).substring(0, 4) + "...");
    	}
        return requestHeader;
    }



    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }



    public int getStatus() {
        return status;
    }



    public void setStatus(int status) {
        this.status = status;
    }



    public Long getBegin() {
		return begin;
	}



	public void setBegin(Long begin) {
		this.begin = begin;
	}



	public Map<String, String> getResponseHeader() {
        return responseHeader;
    }



    public void setResponseHeader(Map<String, String> responseHeader) {
        this.responseHeader = responseHeader;
    }



    public Object getResponseData() {
        return responseData;
    }



    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }
    
   
}