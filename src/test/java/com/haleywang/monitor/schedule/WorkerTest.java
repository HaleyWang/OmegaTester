package com.haleywang.monitor.schedule;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by haley on 2018/12/7.
 */
public class WorkerTest {
    @Test
    public void testRun() throws Exception {

    }

    public static void main(String[] args) {



        ListeningExecutorService listeningExecutorService = MoreExecutors
                .listeningDecorator(Executors.newCachedThreadPool());
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);


        Job job = new SimpleJob() {
            @Override
            public void run() {
                System.out.println("Now begin: " + new Date());
                try {
                    for (int  i = 1; i <= 5; i++) {
                        Thread.sleep(1000);
                        System.out.println("looping." + i);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
                System.out.println("Now end: " + new Date());
            }
        };
        Runnable listener = new Runnable() {
            @Override
            public void run() {
                System.out.println("job done ...");

            }
        };


        Worker worker = new Worker(listeningExecutorService, scheduledExecutorService, job, listener);
        scheduledExecutorService.scheduleAtFixedRate(worker, 0, 2, TimeUnit.SECONDS);

    }

}