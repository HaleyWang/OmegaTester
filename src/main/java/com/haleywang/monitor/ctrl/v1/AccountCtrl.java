package com.haleywang.monitor.ctrl.v1;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.mvc.ParamBody;
import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.EmailPasswordPair;
import com.haleywang.monitor.dto.NewAccountDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.impl.ReqAccountServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.UrlUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;

/**
 * Created by haley on 2018/8/18.
 */
public class AccountCtrl extends BaseCtrl {

    public String publicRegister() throws IOException {
        Map<String, Object> regBody = getBodyParamsToMap();
        String pass = UrlUtils.decode(regBody.get("pass").toString());
        String name = UrlUtils.decode(regBody.get("name").toString());
        String email = UrlUtils.decode(regBody.get("email").toString());

        ReqAccountService reqAccountService = new ReqAccountServiceImpl();
        ResultStatus<ReqAccount> res = reqAccountService.register(name, email, pass);
        return JsonUtils.toJson(res);
    }

    public String publicLogin() throws IOException {
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
        //String token;
        //String pass;
        //String email;

        //TODO
        return new ReqAccountServiceImpl().resetSuperAdminPassword(dto);
    }

    public ResultStatus resetPassword(@ParamBody ResetPasswordDto dto) {
        //String token;
        //String pass;
        //String email;

        //TODO
        return new ReqAccountServiceImpl().resetPassword(dto);
    }

    public ResultStatus changePassword(@ParamBody ChangePasswordDto dto) {
        //String token;
        //String pass;
        //String email;
        return new ReqAccountServiceImpl().changePassword(dto);
    }

    public String create(@ParamBody NewAccountDto dto) {
        ReqAccountService reqAccountService = new ReqAccountServiceImpl();

        ResultStatus<ReqAccount> res = reqAccountService.register(dto);


        //TODO
        return null;
    }

    public ResultStatus<ReqAccount> info() {

        ReqAccount currentAccount = currentAccountAndCheck();
        ReqAccount account = new ReqAccount();
        account.setAccountId(currentAccount.getAccountId());
        account.setName(currentAccount.getName());
        account.setEmail(currentAccount.getEmail());
        ResultStatus<ReqAccount> res = new ResultStatus<>();
        return res.ofData(account);
    }

}
