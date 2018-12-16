package com.haleywang.monitor.schedule;

import com.haleywang.monitor.utils.DateUtils;

import java.util.Date;

/**
 * Created by haley on 2018/12/5.
 */
public abstract class SimpleJob implements Job {



    private String cronExpression;
    private String id;
    private long timeout;
    private Date lastTime;


    public SimpleJob() {}

    public SimpleJob(String id, long timeout, String cronExpression) {
        this.setId(id);
        this.setTimeout(timeout);
        this.setCronExpression(cronExpression);
    }

    @Override
    public Date getLastTime() {
        return DateUtils.copy(lastTime);
    }

    @Override
    public void setLastTime(Date lastTime) {
        this.lastTime = DateUtils.copy(lastTime);

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
