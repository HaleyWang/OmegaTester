package com.haleywang.monitor.schedule;

import java.net.MalformedURLException;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haleywang.monitor.service.impl.ReqJobService;
import lombok.Data;

/**
 * Created by haley on 2018/12/5.
 */
@Data
public class SimpleJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);





    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     *
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        // Say Hello to the World and display the date/time
        LOG.info(" #########  Hello World! - " + new Date());

        String id = context.getJobDetail().getKey().getName();

        ReqJobService reqJobService = new ReqJobService();
        try {
            reqJobService.runBatch(Long.parseLong(id));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

}
