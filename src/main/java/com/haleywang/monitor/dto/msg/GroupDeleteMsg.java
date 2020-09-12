package com.haleywang.monitor.dto.msg;

import com.haleywang.monitor.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author haley
 */

@Getter
public enum  GroupDeleteMsg implements Message {

    /**
     * @author haley
     */
    NOT_ALLOWED("NOT_ALLOWED", "Not allowed");

    private final String description;
    private final String code;

    GroupDeleteMsg(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
