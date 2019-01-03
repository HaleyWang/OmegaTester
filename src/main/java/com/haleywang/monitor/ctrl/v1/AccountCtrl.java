package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.impl.ReqAccountServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.UrlUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.impl.cookie.BasicClientCookie;

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

        BasicClientCookie coo = new BasicClientCookie(Constants.LOGIN_COOKIE, res.getData().getLeft());
        coo.setPath("/");
        addCookie(coo);

        ResultStatus<ReqAccount> result = new ResultStatus<>();
        return JsonUtils.toJson(result.ofData(res.getData().getRight()));
    }

    public String publicLogout() {

        BasicClientCookie coo = new BasicClientCookie(Constants.LOGIN_COOKIE, "");
        coo.setPath("/");
        addCookie(coo);

        ResultStatus<String> res = new ResultStatus<>();

        return JsonUtils.toJson(res);
    }

    public String changePassword() {
        //String token;
        //String pass;
        //String email;

        //TODO
        return null;
    }

    public String forgotPassword() {
        //String token;
        //String pass;
        //String email;

        //TODO
        return null;
    }

    public String info() {
        System.out.println(" ====> ");

        ReqAccount currentAccount = currentAccount();
        ResultStatus<ReqAccount> res = new ResultStatus<>();

        return JsonUtils.toJson(res.ofData(currentAccount));
    }

}
