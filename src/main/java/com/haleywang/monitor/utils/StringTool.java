package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringTool {


    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
