package com.haleywang.monitor.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haley
 * @date 2018/12/16
 */
@Getter
@Setter
public class NewAccountDto extends EmailPasswordPair {
    private String name;
    private String type;
}
