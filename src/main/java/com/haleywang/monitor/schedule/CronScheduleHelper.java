package com.haleywang.monitor.schedule;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.model.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2018/12/5.
 */
public class CronScheduleHelper {

    private static Scheduler scheduler;

    private static final Logger LOG = LoggerFactory.getLogger(CronScheduleHelper.class);
    static long timeout = 30 * 60;


    public static void start() {
        if (scheduler != null) {
            return;
        }

        try {
            // Grab the Scheduler instance from the Factory
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            startSchedule();


            /*

            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/6 * * * * ?"))
                    .build();


            // define the job and tie it to our HelloJob class
            JobDetail job = JobBuilder.newJob(App.HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();
                    */


            // Tell quartz to schedule the job using our trigger
            //scheduler.scheduleJob(job, trigger);

            //Set<JobKey> ss = scheduler.getJobKeys(GroupMatcher.groupEquals("group1"));

            //scheduler.getTriggersOfJob(ss.stream().findFirst().orElse(null)).get(0).getNextFireTime();


            //scheduler.shutdown();

        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
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

            putSchedule(reqBatch);

        }


    }


    public static void putSchedule(ReqBatch reqBatch) {

        if(BooleanUtils.isFalse(reqBatch.getEnable()) || StringUtils.isBlank(reqBatch.getTimeExpression())) {
            return;
        }

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(reqBatch.getBatchId()+"", "trigger_group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(reqBatch.getTimeExpression()))
                .build();


        // define the job and tie it to our HelloJob class
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity(reqBatch.getBatchId()+"", "job_group1")
                .build();


        // Tell quartz to schedule the job using our trigger
        try {

            if(scheduler.checkExists(jobDetail.getKey())) {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }else {
                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}