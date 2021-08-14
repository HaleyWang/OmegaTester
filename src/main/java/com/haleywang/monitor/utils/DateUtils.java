package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author haley
 * @date 2018/12/16
 */

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public class DateUtils {

    public static Date copy(Date aDate) {
        return aDate != null ? new Date(aDate.getTime()) : null;
    }
}
