package com.haleywang.monitor.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

import com.google.common.util.concurrent.*;
import com.haleywang.monitor.common.ReqException;
import org.quartz.CronExpression;

public class ScheduleTool {

    private ListeningScheduledExecutorService service;
    ScheduledExecutorService scheduledExecutorService;
    private Map<String, ScheduledFuture> map = new ConcurrentHashMap<>();

    private static class Instance {
        public static final ScheduleTool INSTANCE = new ScheduleTool();
    }

    public static ScheduleTool instance() {
        return Instance.INSTANCE;
    }

    public void init() {
        service =  MoreExecutors
                .listeningDecorator(Executors.newScheduledThreadPool(10));

        scheduledExecutorService = Executors.newScheduledThreadPool(5);


        Runnable cancelRunnable = new Runnable() {
            @Override
            public void run() {
                for(String key : map.keySet()) {
                    ScheduledFuture sf = map.get(key);
                    if( sf.isCancelled()  || sf.isDone()) {
                        map.remove(key);
                    }
                }
            }
        };
        service.scheduleWithFixedDelay(cancelRunnable, 1000, 1000, TimeUnit.SECONDS);

    }

    public void putSchedule(Job job) {
        if(service == null) {
            init();
        }
        long periodSeconds = 0;
        try {
            periodSeconds = CronUtils.getNextValidSeconds(job);
        } catch (ParseException e) {
            throw new ReqException(e.getMessage(), e);

        }

        if(periodSeconds <= 1) {
            return;
        }

        if(map.containsKey(job.getId())) {
            return;
        }


        ListeningExecutorService listeningExecutorService;

        Runnable completeListener = new Runnable() {
            @Override
            public void run() {
                map.remove(job.getId());
                ScheduleTool.instance().putSchedule(job);
            }
        };

        Worker worker = new Worker(service, scheduledExecutorService, job, completeListener );


        ListenableScheduledFuture<?> jobFuture = service.schedule(worker, periodSeconds, TimeUnit.SECONDS);

        map.put(job.getId(), jobFuture);

    }

    public void cancelSchedule(String jobId) {
        cancelSchedule(jobId, true);
    }

    public void cancelSchedule(String jobId, boolean mayInterruptIfRunning) {
        long cancelTime = 100;
        Runnable cancelRunnable = new Runnable() {
            @Override
            public void run() {
                ScheduledFuture val = map.remove(jobId);
                if(val == null) {
                    return;
                }
                val.cancel(mayInterruptIfRunning);
            }
        };
        service.schedule(cancelRunnable, cancelTime, TimeUnit.MICROSECONDS);
    }

    public void reset() {
        service.shutdownNow();
        init();
    }



}
