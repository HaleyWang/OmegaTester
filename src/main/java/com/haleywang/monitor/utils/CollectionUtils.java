package com.haleywang.monitor.utils;

import com.haleywang.monitor.model.ReqTaskHistory;
import com.haleywang.monitor.model.ReqTaskHistoryMeta;

import java.util.List;

/**
 * Created by haley on 2018/8/18.
 */
public class CollectionUtils {

    public static boolean isEmpty(List<?> hms) {
        return hms == null || hms.size() == 0;
    }

    public static int size(List<?> hList) {
        if(hList == null) {
            return -1;
        }
        return hList.size();
    }
}
