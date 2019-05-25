package com.haleywang.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto extends EmailPasswordPair {
    private String oldPassword;

}
