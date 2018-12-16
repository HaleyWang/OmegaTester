package com.haleywang.monitor.schedule;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by haley on 2018/12/6.
 */
public class CronUtils {

    public static Date getNextValidTimeAfter(String cronExpression, Date date) throws ParseException {
        CronExpression c = new CronExpression(cronExpression);
        return c.getNextValidTimeAfter(date);
    }

    public static Date getNextValidTime(String cronExpression) throws ParseException {
        return getNextValidTimeAfter(cronExpression, new Date());
    }

    public static Long getNextValidSeconds(Job job) throws ParseException {
        Date latTime = job.getLastTime() != null ? job.getLastTime() : new Date();
        Date nextTime = getNextValidTimeAfter(job.getCronExpression(), latTime);
        job.setLastTime(nextTime);
        return (nextTime.getTime() - latTime.getTime()) / 1000;
    }
}
