package com.haleywang.monitor.common;

import java.io.IOException;

public class ReqSqlException extends ReqException {
    public ReqSqlException(IOException e) {
        super(e);

    }
}
