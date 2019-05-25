package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by haley on 2018/12/9.
 */

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public class DateUtils {

    public static Date copy(Date aDate) {
        return aDate != null ? new Date(aDate.getTime()) : null;
    }
}
