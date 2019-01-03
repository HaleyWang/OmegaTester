package com.haleywang.monitor.common.req;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HttpResp {
    private String data;
    private int status;
    private Map<String, String> headers = new HashMap<>();

}