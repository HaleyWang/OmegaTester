package com.haleywang.monitor.common;

import com.haleywang.monitor.dto.Message;

/**
 *
 * @author haley
 * @date 2018/8/19
 */
public enum LoginMsg implements Message {
     NOT_FOUND ("NOT_FOUND") ,
     NOT_ALLOWED ("NOT_ALLOWED") ,
     OK ( "OK"),
     EMAIL_OR_PASSWORD_IS_INCORRECT ( "EMAIL_OR_PASSWORD_IS_INCORRECT"),
     EMAIL_EXISTS ( "EMAIL_EXISTS");


    private final String description;

    LoginMsg(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
