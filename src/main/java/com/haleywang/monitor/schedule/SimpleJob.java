package com.haleywang.monitor.schedule;

import com.haleywang.monitor.service.impl.ReqJobService;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * Created by haley on 2018/12/5.
 */
@Data
public class SimpleJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        String id = context.getJobDetail().getKey().getName();

        ReqJobService reqJobService = new ReqJobService();
        reqJobService.runBatch(Long.parseLong(id));

    }

}
