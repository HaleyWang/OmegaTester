package com.haleywang.monitor.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.haleywang.monitor.common.ReqException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by haley on 2017/3/10.
 */
public class TemplateUtils {

    public static String parse(String temp, JSONObject data) {

        return parse(temp, data.toString());
    }

    public static String parse(String temp, String data) {
        TypeReference<HashMap> t = new TypeReference<HashMap>() {};

        Map map = JsonUtils.fromJson(data, t);

        return parse(temp, map);
    }

    public static String parse(String temp, Map data) {
        if(temp == null) {
            return temp;
        }
        Handlebars handlebars = new Handlebars();

        try {
            Template template = handlebars.compileInline(temp);
            return template.apply(data);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }
}
