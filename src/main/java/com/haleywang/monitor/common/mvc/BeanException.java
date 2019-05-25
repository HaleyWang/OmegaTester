package com.haleywang.monitor.common.mvc;

import com.haleywang.monitor.common.ReqException;

/**
 * Created by haley on 2018/8/17.
 */
public class BeanException extends ReqException {


    public BeanException(String s) {
        super(s);
    }

    public BeanException(String s, Exception e) {
        super(s, e);
    }
}
