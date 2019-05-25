package com.haleywang.monitor.common.logger;


public class ReqMsg{
	private String hostname = "";
	private String clientIp = "";
	private String serviceName = "";

	public String getHostname() {
		return hostname;
	}

	public String getClientIp() {
		return clientIp;
	}

	public String getServiceName() {
		return serviceName;
	}
	
	public String getReqInfo(){
		return getHostname() + " " + getClientIp() + " " + getServiceName();
	}
}
