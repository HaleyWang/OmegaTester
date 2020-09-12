package com.haleywang.monitor.common.req;

/**
 * @author haley
 */
public enum HttpMethod {

    /**
     * Options of enum
     */
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS;

    public static HttpMethod parse(String s) {
        if (s == null) {
            return null;
        }
        for(HttpMethod val : HttpMethod.values()) {
            if (val.name().equalsIgnoreCase(s)) {
                return val;
            }

        }
        return null;
    }
}
