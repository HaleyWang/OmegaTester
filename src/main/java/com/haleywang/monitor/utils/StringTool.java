package com.haleywang.monitor.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class StringTool {

    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
