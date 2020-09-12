package com.haleywang.monitor.ctrl.v1;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.LoginMsg;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.LoginResultDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.impl.ReqAccountServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by haley on 2018/8/18.
 */
public class AccountCtrl extends BaseCtrl {

    public ResultStatus<ReqAccount> publicRegister()  {
        Map<String, Object> regBody = getBodyParamsToMap();
        String pass = UrlUtils.decode(regBody.get("pass").toString());
        String name = UrlUtils.decode(regBody.get("name").toString());
        String email = UrlUtils.decode(regBody.get("email").toString());

        return new ReqAccountServiceImpl().register(name, email, pass);
    }

    public ResultMessage<LoginResultDto, LoginMsg> publicLogin() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> regBody = getBodyParamsToMap();
        String pass = UrlUtils.decode(regBody.get("pass").toString());
        String email = UrlUtils.decode(regBody.get("email").toString());

        ReqAccountService reqAccountService = new ReqAccountServiceImpl();
        ResultMessage<LoginResultDto, LoginMsg> res = reqAccountService.login(email, pass);
        String cookie = Optional.ofNullable(res.getData()).map(LoginResultDto::getLoginCookieVal).orElse(StringUtils.EMPTY);
        addCookie(ImmutableMap.of(Constants.LOGIN_COOKIE, cookie));
        return res;
    }

    public String publicLogout() {

        addCookie(ImmutableMap.of(Constants.LOGIN_COOKIE, ""));

        ResultStatus<String> res = new ResultStatus<>();

        return JsonUtils.toJson(res);
    }

    public ResultStatus resetSuperAdminPassword(@ParamBody ResetPasswordDto dto) {

        //TODO
        return new ReqAccountServiceImpl().resetSuperAdminPassword(dto);
    }

    public ResultStatus resetPassword(@ParamBody ResetPasswordDto dto) {

        //TODO
        return new ReqAccountServiceImpl().resetPassword(dto);
    }

    public ResultStatus changePassword(@ParamBody ChangePasswordDto dto) {

        return new ReqAccountServiceImpl().changePassword(dto);
    }

    public ResultStatus<ReqAccount> info() {

        ReqAccount currentAccount = currentAccountAndCheck();

        return new ResultStatus<>(ReqAccount.builder()
                .accountId(currentAccount.getAccountId())
                .name(currentAccount.getName())
                .email(currentAccount.getEmail())
                .build());
    }

}
