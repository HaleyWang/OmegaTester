package com.haleywang.monitor.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.ReqIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ReqFormatter {


    private static final Map<Pattern, String> REPLACE_PATTERN_MAP = new HashMap<>();


    public static final String HEADERS = "headers";
    public static final String VAR_TEXT = "var";

    static {
        try {
            initMyReqReplace();
        } catch (Exception e) {
            throw new ReqException(e);
        }
    }


    private static void initMyReqReplace() throws IOException {

        String test = FileTool.read("conf/myrequest-replace.json");

        TypeReference<HashMap<String, String>> t = new TypeReference<HashMap<String, String>>() {
        };
        Map<String, String> map = JsonUtils.fromJson(test, t);

        for (Map.Entry<String, String> es : map.entrySet()) {

            REPLACE_PATTERN_MAP.put(Pattern.compile("\"" + es.getKey() + "\"", Pattern.CASE_INSENSITIVE),
                    "\"" + es.getValue() + "\"");
        }

    }

    public String format(String input) {

        String body = input;
        if (body.trim().indexOf(VAR_TEXT) == 0) {
            return body;
        }
        if (body.trim().indexOf('{') < 0) {
            body = "{}";
        }
        if (body.endsWith(",")) {
            String newBody = body.substring(0, body.length() - 1);
            if (!body.endsWith("}")) {
                try {
                    newBody = newBody + "}";
                    JsonUtils.toStandardJson(newBody);
                    body = newBody;
                } catch (JSONException e) {
                    throw ReqIllegalArgumentException.ofPostBodyError();
                }
            }
        }

        String result = JsonUtils.toStandardJson(body);

        for (Map.Entry<Pattern, String> es : REPLACE_PATTERN_MAP.entrySet()) {
            result = es.getKey().matcher(result).replaceAll(es.getValue());
        }

        TypeReference<HashMap<String, Object>> t = new TypeReference<HashMap<String, Object>>() {

        };
        Map<String, Object> dataMap = JsonUtils.fromJson(result, t);
        dataMap.putIfAbsent("name", "");
        dataMap.putIfAbsent("url", "");
        dataMap.putIfAbsent("method", "GET");
        dataMap.putIfAbsent(HEADERS, new HashMap<String, String>(Constants.DEFAULT_MAP_SIZE));
        dataMap.putIfAbsent("body", "");

        if (StringUtils.isBlank(dataMap.getOrDefault("name", "") + "")) {
            String name =  UrlUtils.getPath(dataMap.getOrDefault("url", "") + "");
            name = replaceName(name);
            dataMap.put("name", name);
        }else {
            String name = dataMap.get("name") + "";
            name = replaceName(name);

            dataMap.put("name", name);
        }

        if (dataMap.get(HEADERS) instanceof String) {
            dataMap.put(HEADERS, new HashMap<String, String>(Constants.DEFAULT_MAP_SIZE));
        }
        result = JsonUtils.toJson(dataMap);

        return result;
    }

    private String replaceName(String name) {
        name = name.replaceAll("/\\d+", "/{id}")
                .replaceAll("[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}", "\\{id}");
        return name;
    }
}
