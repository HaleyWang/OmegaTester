package com.haleywang.monitor.utils;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haley on 2017/3/10.
 */
public class TemplateUtilsTest {
    @Test
    public void parse() throws Exception {

        String template = "Hello {{_meta.info}}!";
        Map map = new HashMap();
        Map map1 = new HashMap();
        map1.put("info", "ok");
        map.put("_meta", map1);

        Assert.assertEquals("Hello ok!", TemplateUtils.parse(template, map));

    }


    @Test
    public void parse2() throws Exception {

        String template = "Hello {{_meta.info}}!";
        JSONObject map = new JSONObject();

        String str = "";
        JSONObject map1 = new JSONObject();
        map1.put("info", "ok");
        map.put("_meta", map1);


        Assert.assertEquals("Hello ok!", TemplateUtils.parse(template, map));
    }

}