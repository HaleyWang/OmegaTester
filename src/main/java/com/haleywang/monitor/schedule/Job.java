package com.haleywang.monitor.schedule;

import java.util.Date;

public interface Job extends Runnable {
        void setId(String id);
        String getId();

        void setTimeout(long id);
        long getTimeout();

    String getCronExpression();

    void setCronExpression(String cronExpression);


    Date getLastTime();

    void setLastTime(Date time);
}