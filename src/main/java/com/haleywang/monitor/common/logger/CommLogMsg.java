package com.haleywang.monitor.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haleywang.monitor.AppContext;

public class CommLogMsg {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommLogMsg.class);

	public static void logUserId() {
		if (LOG == null) {
			return;
		}
		LOG.info( "(currentAccount for:(CommLogMsg):" + AppContext.getRequestId() + " is:" + AppContext.getAccountId() + ")");
	}
	
	public static void logServerMsg() {
		if (LOG == null) {
			return;
		}
		
		StringBuffer sbuf = new StringBuffer();
		String space = CustomMessageConverter.SPACE;
		//ReqMsg reqMsg = CustomMessageConverter.getReqMsg();
		ReqMsg reqMsg = new ReqMsg();
		sbuf .append("client_ip=").append("\"").append(reqMsg.getClientIp()).append("\"").append(space);
		sbuf.append("hostname=").append("\"").append(reqMsg.getHostname()).append("\"").append(space);
		sbuf.append("service_name=").append("\"").append(reqMsg.getServiceName()).append("\"").append(space);
		sbuf.append("service_version=").append("\"").append(AppContext.getVersion()).append("\"").append(space);
		
		LOG.info( "(serverMsg for:(CommLogMsg):" + AppContext.getRequestId() + " is:" + AppContext.getAccountId() + ")");
	}
	


}
