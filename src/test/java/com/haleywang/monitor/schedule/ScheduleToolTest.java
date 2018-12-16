package com.haleywang.monitor.schedule;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by haley on 2018/12/8.
 */
public class ScheduleToolTest {
    @Test
    public void putSchedule() throws Exception {

    }

    public static void main(String[] args) {


        /*

        Job job = new SimpleJob("aa", 2, "0/6 * * * * ?") {

            @Override
            public void run() {
                System.out.println(this.getId() + new Date());

            }
        };

        ScheduleTool.instance().putSchedule(job);
        //ScheduleTool.instance().cancelSchedule("aa");

        new Thread(){
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(30L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("cancelSchedule");
                ScheduleTool.instance().cancelSchedule("aa");

            }
        }.start();
        */
    }

}