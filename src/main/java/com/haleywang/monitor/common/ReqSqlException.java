package com.haleywang.monitor.common;

import java.sql.SQLException;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ReqSqlException extends ReqException {
    public ReqSqlException(SQLException e) {
        super(e);

    }
}
