package com.haleywang.monitor.common.req;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haley
 * @date 2018/12/16
 */
@Getter
@Setter
public class HttpRequestItem {
	public static final String HTTP = "http";
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
		if (host.indexOf(HTTP) < 0) {
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