package com.haleywang.monitor.dto;

import com.haleywang.monitor.common.req.HttpMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author haley
 * @date 2018/12/16
 */

@Getter
@Setter
public class MyRequest {
    private String name;

    private String url;
    private HttpMethod method;
    private String body;
    private Map<String, String> headers;

}
