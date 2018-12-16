package com.haleywang.monitor.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by haley on 2017/3/10.
 */
public class JsonUtilsTest {

    @Test
    public void toStandardJson() throws Exception {
        String str = JsonUtils.toStandardJson("{'phonetype':\"N'95\",'cat':'WP'}");
        Assert.assertEquals("{\"phonetype\":\"N'95\",\"cat\":\"WP\"}", str);
    }

    @Test
    public void testJson() throws Exception {
        JSONObject jsonOne = new JSONObject();
        JSONObject jsonTwo = new JSONObject();

        jsonOne.put("name", "kewen");
        jsonOne.put("age", "24");

        jsonTwo.put("hobbit", "Dota");
        jsonTwo.put("hobbit2", "wow");
        jsonTwo.put("name", "jim");

        JSONObject jsonThree = JsonUtils.extend(jsonOne, jsonTwo);

        System.out.println(jsonThree.toString());

        TypeReference<HashMap> t = new TypeReference<HashMap>() {
        };

        String envStr = "{\"host\": \"user.demo.com\", \"aa\": {\"a\":1}}";

        HashMap<String, Objects> map = JsonUtils.fromJson(envStr, t);
        System.out.println(map);

    }

}