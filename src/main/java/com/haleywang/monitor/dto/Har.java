package com.haleywang.monitor.dto;

import java.util.List;

import lombok.Data;

@Data
public class Har {

    private HarLog log;


    @Data
    public static class HarLog {
        private List<HarEntry> entries;
    }

    @Data
    public static class HarEntry {
        private HarRequest request;
    }
    @Data
    public static class HarRequest {
        private String method;
        private String url;
        private Integer headersSize;
        private Integer bodySize;
        private List<NameValue> headers;
        private List<NameValue> queryString;
        private List<HarCookie> cookies;
    }

    @Data
    public static class HarCookie extends NameValue {
        private String expires;
        private Boolean httpOnly;
        private Boolean secure;
    }
}
