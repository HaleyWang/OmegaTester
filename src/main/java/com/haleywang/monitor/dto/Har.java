package com.haleywang.monitor.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
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

    @Getter
    @Setter
    public static class HarCookie extends NameValue {
        private String expires;
        private Boolean httpOnly;
        private Boolean secure;
    }
}
