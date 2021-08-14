package com.haleywang.monitor.common.mvc;

import com.haleywang.monitor.common.ReqException;


/**
 * @author haley
 */
public class BeanException extends ReqException {


    public BeanException(String s) {
        super(s);
    }

    public BeanException(String s, Exception e) {
        super(s, e);
    }
}
