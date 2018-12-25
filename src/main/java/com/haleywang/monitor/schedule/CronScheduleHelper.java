package com.haleywang.monitor.schedule;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.model.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import com.haleywang.monitor.service.impl.ReqJobService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by haley on 2018/12/5.
 */
public class CronScheduleHelper {

    private static final BlockingQueue<ReqBatch> REQ_BATCH_QUEUE = new LinkedBlockingDeque();

    private static final Logger LOG = LoggerFactory.getLogger(CronScheduleHelper.class);
    static long timeout = 30 * 60;


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

                try {
                           while(true) { putSchedule(REQ_BATCH_QUEUE.take()); }
                } catch (InterruptedException e) { /* handle */
                    e.printStackTrace();
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    public static void addSchedule(ReqBatch reqBatch) {
        try {
            REQ_BATCH_QUEUE.put(reqBatch);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startSchedule() {



        List<ReqBatch> list = new ArrayList<>();
        try {
            ReqBatchService reqBatchService = new ReqBatchServiceImpl();

            //reqBatchService.initDb();
            Example example = Example.builder(ReqBatch.class).build();
            example.createCriteria().andEqualTo("enable", "true");
            list.addAll(reqBatchService.findAll(example));
        }finally {
            DBUtils.closeSession(true);
        }


        for( ReqBatch reqBatch : list) {
            if(BooleanUtils.isFalse(reqBatch.getEnable()) || StringUtils.isBlank(reqBatch.getTimeExpression())) {
                continue;
            }
            putSchedule(reqBatch);

        }


    }

    private static void putSchedule(ReqBatch reqBatch) {
        putSchedule(new SimpleJob(reqBatch.getBatchId()+"", timeout, reqBatch.getTimeExpression()) {
            @Override
            public void run() {
                ReqJobService ReqJobService = new ReqJobService();
                try {
                    ReqJobService.runBatch(reqBatch.getBatchId());

                } catch (MalformedURLException e) {
                    LOG.error(e.getMessage(), e);
                    throw new ReqException(e.getMessage(), e);

                } catch (UnirestException e) {
                    LOG.error(e.getMessage(), e);

                    throw new ReqException(e.getMessage(), e);

                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    throw new ReqException(e.getMessage(), e);

                }finally {
                    DBUtils.closeSession(true);
                }

            }
        });
    }

}