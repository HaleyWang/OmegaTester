package com.haleywang.monitor.common.req;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * @author haley
 * @date 2018/12/16
 */
@Data
public class HttpResp {
    private String data;
    private int status;
    private Map<String, String> headers = new HashMap<>();

}