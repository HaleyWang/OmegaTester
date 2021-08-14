package com.haleywang.monitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haleywang.monitor.entity.ReqAccount;
import lombok.Getter;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author haley
 * @date 2018/12/16
 */
@Getter
public class LoginResultDto extends  ReqAccount {

    @JsonIgnore
    private final String loginCookieVal;

    public LoginResultDto(String loginCookieVal, ReqAccount a) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        this.loginCookieVal = loginCookieVal;
        PropertyUtils.copyProperties(this, a);
    }


}
