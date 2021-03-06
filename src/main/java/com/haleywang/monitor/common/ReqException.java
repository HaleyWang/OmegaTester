package com.haleywang.monitor.common;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ReqException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -9086697309592524885L;

    public ReqException() {
        super();
    }

    public ReqException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReqException(String message) {
        super(message);
    }

    public ReqException(Throwable cause) {
        super(cause);
    }

}