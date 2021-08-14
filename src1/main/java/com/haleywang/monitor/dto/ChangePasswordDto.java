package com.haleywang.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author haley
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDto extends EmailPasswordPair {
    private String oldPassword;

}
