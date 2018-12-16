package com.haleywang.monitor.schedule;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Worker implements Runnable {
    ListeningExecutorService listeningExecutorService;
    ScheduledExecutorService scheduledExecutorService;
    Job job;
    Runnable listener;


    public Worker(ListeningExecutorService listeningExecutorService, ScheduledExecutorService scheduledExecutorService, Job job, Runnable listener) {
        this.listeningExecutorService = listeningExecutorService;
        this.scheduledExecutorService = scheduledExecutorService;
        this.job = job;
        this.listener = listener;
    }

    @Override
    public void run() {
        ListenableFuture future = listeningExecutorService.submit(job);
        Futures.withTimeout(future, job.getTimeout(), TimeUnit.SECONDS, scheduledExecutorService);
        future.addListener(listener,listeningExecutorService);
    }



}