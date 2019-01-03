package com.haleywang.monitor.dto;

import java.util.Map;

import com.mashape.unirest.http.HttpMethod;
import lombok.Data;

@Data
public class MyRequest {
    private String name;

    private String url;
    private HttpMethod method;
    private String body;
    private Map<String, String> headers;

}
