package com.haleywang.monitor.ctrl.v1;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.impl.ReqAccountServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.UrlUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

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

    public String publicLogin()  {
        Map<String, Object> regBody = getBodyParamsToMap();
        String pass = UrlUtils.decode(regBody.get("pass").toString());
        String email = UrlUtils.decode(regBody.get("email").toString());

        ReqAccountService reqAccountService = new ReqAccountServiceImpl();

        ResultStatus<Pair<String, ReqAccount>> res = reqAccountService.login(email, pass);

        addCookie(ImmutableMap.of(Constants.LOGIN_COOKIE, res.getData().getLeft()));

        ResultStatus<ReqAccount> result = new ResultStatus<>();
        return JsonUtils.toJson(result.ofData(res.getData().getRight()));
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
