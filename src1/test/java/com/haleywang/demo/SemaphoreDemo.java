package com.haleywang.demo;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    //在一些资源有限的场景下很有实用性，比如数据库连接，停车场,厕所,设置共享文件的最大客户端访问个数,...
    public static void main(String[] args) {

        //一个客车拉了22人到一个只有5个坑的厕所.
        ExecutorService exec = Executors.newCachedThreadPool();
        //1.permits: 最大访问线程数, 厕所有5个坑.
        int permits = 5;
        final Semaphore semaphore = new Semaphore(permits);

        for (int i = 0; i < 22; i++) {

            final int NO = i;

            exec.execute(() -> {

                try {
                    //2.从信号量获取一个许可，如果无可用许可前 将一直阻塞等待.
                    semaphore.acquire();

                    System.out.println("in: " + NO);
                    Thread.sleep((long) (Math.random() * 10000));

                    //Returns the current number of permits available in this semaphore.
                    //获取当前信号量可用的许可.
                    System.out.println("availablePermits: " + semaphore.availablePermits());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("out: " + NO);
                    // release会唤醒一个等待在Semaphore上的一个线程来尝试获得许可
                    // 释放一个许可，别忘了在finally中使用，注意：多次调用该方法，会使信号量的许可数增加，达到动态扩展的效果，
                    // 如：初始permits 为1， 调用了两次release，最大许可会改变为2
                    semaphore.release();
                }
            });
        }

        //所有人都上完WC了
        while (semaphore.availablePermits() < permits) {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
        System.out.println("availablePermits: " + semaphore.availablePermits());
        System.out.println("end-----------------");
    }

}

