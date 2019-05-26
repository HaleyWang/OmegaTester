package com.haleywang.monitor;

import com.haleywang.monitor.common.mvc.Server;
import com.haleywang.monitor.schedule.CronScheduleHelper;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;

public class App {

    public static void main( String[] args) throws Exception {
        new ReqBatchServiceImpl().initDb(false);
        Server.start(args);
        CronScheduleHelper.start();

    }


}
