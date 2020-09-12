package com.haleywang.monitor.common;
/**
 * @author haley
 * @date 2018/12/16
 */
public class NotFoundException  extends ReqException {

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
