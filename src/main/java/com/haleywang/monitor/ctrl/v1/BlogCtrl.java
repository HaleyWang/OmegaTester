package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.mvc.BaseCtrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haley on 2018/8/17.
 */
public class BlogCtrl extends BaseCtrl {

    public String getBologs() {

        return "{\"name\": \"abc\"}";
    }

    public String cookie() {

        Map<String, String> map = new HashMap<>();
        map.put("aaaa", "bbbb");
        addCookie(map);

        return "{\"name\": \"abc\"}";
    }
}
