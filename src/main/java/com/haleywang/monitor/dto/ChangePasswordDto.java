package com.haleywang.monitor.dto;

import lombok.Data;

@Data
public class ChangePasswordDto extends EmailPasswordPair {
    private String oldPassword;

}
