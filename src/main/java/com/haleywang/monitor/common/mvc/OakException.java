package com.haleywang.monitor.common.mvc;

/**
 * Created by haley on 2018/8/17.
 */
public class OakException extends RuntimeException {


    public OakException(String s) {
        super(s);
    }

    public OakException(String s, Exception e) {
        super(s, e);
    }
}
