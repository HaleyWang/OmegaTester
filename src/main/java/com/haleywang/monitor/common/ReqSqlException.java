package com.haleywang.monitor.common;

import java.sql.SQLException;

public class ReqSqlException extends ReqException {
    public ReqSqlException(SQLException e) {
        super(e);

    }
}
