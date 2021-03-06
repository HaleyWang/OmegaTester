package com.haleywang.monitor.schedule;

import com.haleywang.monitor.service.impl.ReqJobService;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haley
 * @date 2018/12/16
 */
@Data
public class SimpleJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        String id = context.getJobDetail().getKey().getName();

        ReqJobService reqJobService = new ReqJobService();
        reqJobService.runBatch(Long.parseLong(id));

    }

}
