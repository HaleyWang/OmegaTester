package com.haleywang.monitor.common.req;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class HttpRequestItem {
	private String host;
	private String path;
    private StringBuilder dataBuff;
    private HttpMethod httpMethod = HttpMethod.GET;
    private Map<String, String> reqHeaders = new HashMap<>();
    
	public String getUrl() {
		return host + path;
	}


	public void addReqHeader(String key, String value) {
		reqHeaders.put(key, value);
	}
	public void setHost(String host) {
		if(host.indexOf("http") < 0) {
			host = "http://" + host;
		}
		this.host = host;
	}

	public void appendData(String str) {
		if(dataBuff == null) {
			dataBuff = new StringBuilder();
		}
		dataBuff.append(str);
	}
	
	public StringBuilder getDataBuff() {
		if(dataBuff == null) {
			dataBuff = new StringBuilder();
		}
		return dataBuff;
	}
    
}