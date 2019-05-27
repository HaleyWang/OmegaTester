package com.haleywang.monitor.common.req;

public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS;

    public static HttpMethod parse(String s) {
        if(s == null) {
            return null;
        }
        String input = s.toUpperCase();
        for(HttpMethod val : HttpMethod.values()) {
            if(val.name().equals(s)) {
                return val;
            }

        }
        return null;
    }
}
