package com.haleywang.monitor.schedule;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.model.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import com.haleywang.monitor.service.impl.ReqInfoServiceImpl;
import com.haleywang.monitor.service.impl.ReqJobService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by haley on 2018/12/5.
 */
public class CronScheduleHelper {




    public static void putSchedule(Job job)  {
        try {
            ScheduleTool.instance().putSchedule(job);
        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public static void start() {

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                startSchedule();
            }
        });

        t.setDaemon(true);
        t.start();
    }

    private static void startSchedule() {

        long timeout = 30 * 60;

        List<ReqBatch> list = new ArrayList<>();
        try {
            ReqBatchService reqBatchService = new ReqBatchServiceImpl();
            list.addAll(reqBatchService.findAll());
        }finally {
            DBUtils.closeSession(true);
        }


        for( ReqBatch reqBatch : list) {
            if(BooleanUtils.isFalse(reqBatch.getEnable()) || StringUtils.isBlank(reqBatch.getTimeExpression())) {
                continue;
            }
            putSchedule(new SimpleJob(reqBatch.getBatchId()+"", timeout, reqBatch.getTimeExpression()) {
                @Override
                public void run() {
                    ReqJobService ReqJobService = new ReqJobService();
                    try {
                        ReqJobService.runBatch(reqBatch);

                    } catch (MalformedURLException e) {
                        throw new ReqException(e.getMessage(), e);

                    } catch (UnirestException e) {
                        throw new ReqException(e.getMessage(), e);


                    }finally {
                        DBUtils.closeSession(true);
                    }

                }
            });

        }


    }

}