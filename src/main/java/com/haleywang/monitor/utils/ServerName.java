package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerName {


    private static String localServerName;

    static {
        try {
            localServerName = InetAddress.getLocalHost().getHostName();
            //linux
        } catch (UnknownHostException e) {
            //TODO -haley

        }
        if (null == localServerName) {
            //win
            localServerName = System.getenv().get("COMPUTERNAME");

        }
    }
    
    public static String getLocalServerName() {
    	return localServerName;
    }

}
