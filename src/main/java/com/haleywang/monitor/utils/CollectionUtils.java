package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author haley
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    public static boolean isEmpty(List<?> hms) {
        return hms == null || hms.isEmpty();
    }

    public static int size(List<?> hList) {
        if(hList == null) {
            return -1;
        }
        return hList.size();
    }
}
