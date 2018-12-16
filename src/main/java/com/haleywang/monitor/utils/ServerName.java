package com.haleywang.monitor.utils;

import com.haleywang.monitor.common.ReqException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerName {
	
	private static String localServerName;

    static{
        try {
            localServerName = InetAddress.getLocalHost().getHostName(); //linux
        } catch (UnknownHostException e) {
            //TODO -haley

        }
        if(null == localServerName){
            localServerName = System.getenv().get("COMPUTERNAME");  //win
        }
    }
    
    public static String getLocalServerName() {
    	return localServerName;
    }

}
