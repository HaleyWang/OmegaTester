package com.haleywang.monitor.dto;

import lombok.Data;

@Data
public class NewAccountDto extends EmailPasswordPair {
    private String name;
    private String type;
}
