package com.haleywang.monitor.common;

public class NotFoundException  extends ReqException {

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
