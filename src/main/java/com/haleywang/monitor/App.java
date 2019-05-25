package com.haleywang.monitor;

import com.haleywang.monitor.common.mvc.Server;
import com.haleywang.monitor.schedule.CronScheduleHelper;

public class App {

    public static void main( String[] args) throws Exception {
        Server.start(args);
        CronScheduleHelper.start();

    }


}
