package com.haleywang.monitor.common.mvc;

import com.google.common.base.Splitter;
import com.haleywang.db.DBUtils;
import com.haleywang.monitor.AppContext;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.login.LoginCookieDecrypt;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqBatchHistory;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.service.impl.ReqAccountServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ApiHandler implements HttpHandler {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static Pattern COOKIE_SPLITE = Pattern.compile("(?<![=])=(?![=])");

    private static final int STATUS_404 = 404;
    private static final int STATUS_500 = 500;

    public static final Logger LOG = LoggerFactory.getLogger(ApiHandler.class);

    @Override
    public void handle(HttpExchange he) throws IOException {
        boolean success = false;
        try {
            String uri = he.getRequestURI().getPath();
            //final Headers headers = he.getResponseHeaders();
            // get cookie
            //List<String> cookies = he.getRequestHeaders().get("Cookie");


            String[] paths = uri.split("/");
            if (paths.length < 4) {
                response404(he);
                throw new ReqException("404");
            }

            if (paths[2].startsWith("public")) {
                he.setAttribute("public", "1");
            } else {
                he.setAttribute("public", "0");
            }

            filter(he);

            BaseCtrl ctrl = CtrlFactory.of(paths[2]);
            ctrl.doService(he);
            success = true;

        } catch (NoSuchMethodException e) {
            response404(he);
            LOG.error("ApiHandler NoSuchMethodException", e);
        } catch (Exception e) {
            response500(he, e);
            LOG.error("ApiHandler Exception", e);
        } finally {
            DBUtils.closeSession(success);

            he.close();
        }
    }

    private void response500(HttpExchange he, Exception e) {
        String result = e == null || e.getMessage() == null ? "error" : e.getMessage();
        if(e instanceof InvocationTargetException) {
            result = ((InvocationTargetException) e).getTargetException().getMessage();
        }
        int status = STATUS_500;

        ResultStatus rs = new ResultStatus().ofMsg(result);
        JsonUtils.toJson(rs);

        responseData(he, JsonUtils.toJson(rs), status);
    }

    private void response404(HttpExchange he) {
        String result = "not found";
        int status = STATUS_404;

        ResultStatus rs = new ResultStatus().ofMsg(result);
        responseData(he, JsonUtils.toJson(rs), status);
    }

    private void responseData(HttpExchange he, String result, int status) {
        final Headers headers = he.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        final byte[] rawResponseBody = result.getBytes(CHARSET);
        OutputStream ops = null;
        try {
            he.sendResponseHeaders(status, rawResponseBody.length);
            ops = he.getResponseBody();
            ops.write(rawResponseBody);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(ops);
        }
    }

    private static void filter(HttpExchange t) throws IOException {

        AppContext.setAccountId(null);
        //ReqAccount res = reqAccountService.findOne(id);
        t.setAttribute(Constants.CURRENT_ACCOUNT, null);
        //Long id = 1L;
        ReqAccountService reqAccountService = new ReqAccountServiceImpl();
        //ReqAccount res = reqAccountService.findOne(id);
        //t.setAttribute(Constants.CURRENT_ACCOUNT, res);

        parseCurrentUser(reqAccountService, t);

    }

    private static ReqAccount parseCurrentUser(ReqAccountService reqAccountService, HttpExchange t) {
        if ("1".equals(t.getAttribute("public"))) {
            return null;
        }

        List<String> cookies = t.getRequestHeaders().get("Cookie");

        if (cookies == null) {
            return null;
        }
        String loginCookieVal = null;
        for (String coo : cookies) {

            Map<String, String> cooMap = Splitter.onPattern(";\\s*").withKeyValueSeparator(Splitter.on(COOKIE_SPLITE))
                    .split(coo);
            if (cooMap.containsKey(Constants.LOGIN_COOKIE)) {
                loginCookieVal = cooMap.get(Constants.LOGIN_COOKIE);
                break;
            }
        }
        if (loginCookieVal == null || "".endsWith(loginCookieVal)) {
            return null;
        }

        LoginCookieDecrypt loginCookieDecrypt = new LoginCookieDecrypt(loginCookieVal);
        Long accountId = loginCookieDecrypt.getAccountId();
        if (accountId == null) {
            return null;
        }
        ReqAccount acc = reqAccountService.findOne(accountId);
        String email = loginCookieDecrypt.getEmail(acc);
        if (email == null) {
            return null;
        }
        if (acc.getAccountId() == null) {
            return null;
        }
        //request.setAttribute(Constants.CURRENT_ACCOUNT, acc);
        AppContext.setAccountId(acc.getAccountId());
        //ReqAccount res = reqAccountService.findOne(id);
        t.setAttribute(Constants.CURRENT_ACCOUNT, acc);
        return acc;
    }

}
