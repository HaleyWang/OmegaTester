package com.haleywang.monitor.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.ReqIllegalArgumentException;
import com.haleywang.monitor.dto.ResultStatus;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ReqFormatter {


    private static final Map<Pattern, String> REPLACE_PATTERN_MAP = new HashMap<>();


    public static final String HEADERS = "headers";

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

    public ResultStatus<String> format(String input) {
        ResultStatus<String> res = new ResultStatus<>();

        String body = input;
        if (body.indexOf("var") == 0) {
            return res.ofData(body);
        }
        if (body.indexOf('{') < 0) {
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
        dataMap.putIfAbsent(HEADERS, new HashMap<String, String>());
        dataMap.putIfAbsent("body", "");

        if (StringUtils.isBlank(dataMap.getOrDefault("name", "") + "")) {
            dataMap.put("name", UrlUtils.getPath(dataMap.getOrDefault("url", "") + ""));
        }

        if (dataMap.get(HEADERS) instanceof String) {
            dataMap.put(HEADERS, new HashMap<String, String>());
        }
        result = JsonUtils.toJson(dataMap);

        return res.ofData(result);
    }
}
