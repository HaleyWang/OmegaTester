package com.haleywang.monitor.utils;

import java.util.Date;

/**
 * Created by haley on 2018/12/9.
 */
public class DateUtils {

    public static Date copy(Date aDate) {
        return aDate != null ? new Date(aDate.getTime()) : null;
    }
}
