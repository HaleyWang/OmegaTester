package com.haleywang.monitor.common.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.utils.CollectionUtils;
import com.haleywang.monitor.utils.JsonUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by haley on 2018/8/17.
 */
public abstract class BaseCtrl {


    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS + "," + METHOD_POST;

    Map<String, List<String>> requestParameters;

    HttpExchange exchange;
    String methodName = "";

    public final void doService(HttpExchange exchange)
            throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.exchange = exchange;

        String uri = exchange.getRequestURI().getPath();
        final Headers headers = exchange.getResponseHeaders();
        requestParameters = getRequestParameters(exchange.getRequestURI());
        // get cookie

        String[] paths = uri.split("/");

        methodName = paths[3];

        final String requestMethod = exchange.getRequestMethod().toUpperCase();
        switch (requestMethod) {
        case METHOD_GET: {
            // do something with the request parameters
            String responseBody = toString(doGet());
            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
            final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
            exchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
            OutputStream ops = exchange.getResponseBody();
            ops.write(rawResponseBody);
            ops.close();
            break;
        }
        case METHOD_OPTIONS: {
            headers.set(HEADER_ALLOW, ALLOWED_METHODS);
            exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
            break;
        }
        default: {

            final String responseBody = toString( doPost());

            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
            final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
            exchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
            OutputStream ops = exchange.getResponseBody();
            ops.write(rawResponseBody);
            ops.close();

            break;
        }
        }

    }

    private String toString(Object doGet) {
        return doGet == null ? null : doGet.toString();
    }

    public static String readRequestBody(HttpExchange exchange) throws IOException {

        // determine encoding
        String encoding = CHARSET.name();
        InputStream in = exchange.getRequestBody();
        String body = IOUtils.toString(in, encoding);
        IOUtils.closeQuietly(in);
        return body;
    }

    public final Object doGet() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod();

    }

    public final Object doPost() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod();
    }

    private static final Set<Class> BASIC_TYPE = new HashSet<>();
    static {
        BASIC_TYPE.addAll(Arrays.asList(
                Byte.class, Short.class, Integer.class, Long.class,
                Float.class, Double.class, Boolean.class, Character.class
        ));
    }

    private Object[] getMethodParams(Parameter[] params) {
        return Arrays.stream(params).map(p -> {

            String name = p.getName();

            String value = getUrlParam(name);

            boolean hasParamBodyAnn = Arrays.stream(p.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(ParamBody.class));

            Object paramVal = null;
            if(hasParamBodyAnn) {
                paramVal = JsonUtils.fromJson(getBodyParams(), p.getType());
            }
            else if(BASIC_TYPE.contains(p.getType())) {

                try {
                    paramVal = params[0].getType().getConstructor( String.class ).newInstance( value);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new ReqException(e);
                }

            }else if (String.class.equals(p.getType())){
                paramVal = value;
            }else {
                paramVal = JsonUtils.fromJson(value, p.getType());
            }
            return paramVal;

        }).collect(Collectors.toList()).toArray();

    }


    private Object invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Arrays.stream(this.getClass().getDeclaredMethods()).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
        if(method == null) {
            throw new NoSuchMethodException("methodName:" + methodName);
        }
        Parameter[] params = method.getParameters();

        Object[] args = params.length == 0 ? new Object[] {} : getMethodParams(params);
        Object obj = method.invoke(this, args);


        if(obj == null) {
            return null;
        }
        if(obj instanceof String) {
            return obj;
        }
        return JsonUtils.toJson(obj);
    }


    static final String URI_PATH = "/";
    static final int SESSION_ID = 12345;


    public final void addCookie(Map<String, String> cookies) {

        // return some cookies so we can check getHeaderField(s)
        Headers respHeaders = exchange.getResponseHeaders();
        List<String> values = new ArrayList<>();

        for (Map.Entry<String, String> item : cookies.entrySet()) {
            values.add(item.getKey() + "=" + item.getValue() + "; Path=" + URI_PATH);
        }

        values.add("ID=JOEBLOGGS; version=1; Path=" + URI_PATH);
        values.add("NEW_JSESSIONID=" + (SESSION_ID + 1) + "; version=1; Path=" + URI_PATH + "; HttpOnly");
        values.add("NEW_CUSTOMER=WILE_E_COYOTE2; version=1; Path=" + URI_PATH);

        respHeaders.put("Set-Cookie", values);

    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue =
                        requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

    public final String getUrlParam(String key) {

        return getUrlParam(key, null);
    }

    public final String getUrlParam(String key, String defaultVal) {
        List<String> a = requestParameters.get(key);
        if (CollectionUtils.isEmpty(a)) {
            return defaultVal;

        }
        return a.get(0) != null ? a.get(0) : defaultVal;
    }

    public final List<String> getUrlParams(String key) {
        return requestParameters.get(key);
    }

    public final <T> T getBodyParams(Class<T> type) {
        try {
            String reqBody = readRequestBody(exchange);
            return JsonUtils.fromJson(reqBody, type);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public final String getBodyParams() {
        try {
            return readRequestBody(exchange);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public final <T> T getBodyParams(TypeReference<T> type) {
        try {
            String reqBody = readRequestBody(exchange);
            return JsonUtils.fromJson(reqBody, type);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public final Map<String, Object> getBodyParamsToMap() {
        try {
            String reqBody = readRequestBody(exchange);
            if (!reqBody.startsWith("{")) {
                return new HashMap<>(Splitter.on("&").withKeyValueSeparator("=").split(reqBody));
            }
            TypeReference<HashMap<String, Object>> t = new TypeReference<HashMap<String, Object>>() {
            };
            return JsonUtils.fromJson(reqBody, t);
        } catch (IOException e) {
            throw new ReqException(e.getMessage(), e);

        }
    }

    public Object getReqAttribute(String key) {
        return exchange.getAttribute(key);
    }

    public ReqAccount currentAccount() {
        return (ReqAccount) getReqAttribute(Constants.CURRENT_ACCOUNT);
    }

    public ReqAccount currentAccountAndCheck() {
        ReqAccount acc = currentAccount();
        Preconditions.checkNotNull(acc, "Current account must be not null");
        return acc;
    }

    public void clearAccount() {
        exchange.setAttribute(Constants.CURRENT_ACCOUNT, null);
    }

}
