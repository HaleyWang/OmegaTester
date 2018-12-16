package com.haleywang.monitor;

import com.haleywang.monitor.mvc.Server;
import com.haleywang.monitor.schedule.CronScheduleHelper;

public class App {

    public static void main(String[] args) throws Exception {
        CronScheduleHelper.start();
        new Server().start(args);
    }


}
