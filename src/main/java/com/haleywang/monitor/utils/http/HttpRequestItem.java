package com.haleywang.monitor.utils.http;

import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpMethod;
import lombok.Data;

@Data
public class HttpRequestItem {
	private String host;
	private String path;
    private StringBuffer dataBuff;
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
			dataBuff = new StringBuffer();
		}
		dataBuff.append(str);
	}
	
	public StringBuffer getDataBuff() {
		if(dataBuff == null) {
			dataBuff = new StringBuffer();
		}
		return dataBuff;
	}
    
}