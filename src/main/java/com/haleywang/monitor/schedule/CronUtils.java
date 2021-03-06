package com.haleywang.monitor.schedule;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * @author haley
 * @date 2018/12/16
 */
public class CronUtils {

    private CronUtils(){}

    public static Date getNextValidTimeAfter(String cronExpression, Date date) throws ParseException {
        CronExpression c = new CronExpression(cronExpression);
        return c.getNextValidTimeAfter(date);
    }

    public static Date getNextValidTime(String cronExpression) throws ParseException {
        return getNextValidTimeAfter(cronExpression, new Date());
    }

}
