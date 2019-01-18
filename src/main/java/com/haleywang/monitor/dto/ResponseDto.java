package com.haleywang.monitor.dto;



/**
 * Created by haley on 2017/7/20.
 */
public class ResponseDto<T> {

    String code;
    String msg;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
