package com.haleywang.monitor.dto;

import lombok.Data;
/**
 * @author haley
 * @date 2018/12/16
 */
@Data
public class IdValuePair {


    private int id;
    private String value;

    public IdValuePair of(int idx, String text) {
        setId(idx);
        setValue(text);
        return this;
    }
}
