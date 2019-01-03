package com.haleywang.monitor.ctrl.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.common.req.AnnoManageUtil;
import com.haleywang.monitor.common.req.ConverterBuilder;
import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqGroup;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.impl.ReqGroupServiceImpl;
import com.haleywang.monitor.service.impl.ReqInfoServiceImpl;
import com.haleywang.monitor.utils.AnnotationUtils;
import com.haleywang.monitor.utils.FileTool;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.UrlUtils;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by haley on 2018/8/18.
 */
public class ReqCtrl extends BaseCtrl {

    private static final Map<Pattern, String> REPLACE_PATTERN_MAP = new HashMap<>();

    private static final List<String> IMPORT_TYPE = new ArrayList<>();
    private static final List<String> EXPORT_TYPE = new ArrayList<>();

    static {
        try {
            initMyReqReplace();
            initImportType();
            initExportType();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initExportType() {
        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestExportAnnotation.class);
        for (Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestExportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            EXPORT_TYPE.add(aName);
        }
    }

    private static void initImportType() {
        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestImportAnnotation.class);
        for (Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestImportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            IMPORT_TYPE.add(aName);
        }
    }

    private static void initMyReqReplace() {
        try {
            String test = FileTool.read("conf/myrequest-replace.json");

            TypeReference<HashMap<String, String>> t = new TypeReference<HashMap<String, String>>() {
            };
            Map<String, String> map = JsonUtils.fromJson(test, t);

            for (Map.Entry<String, String> es : map.entrySet()) {

                REPLACE_PATTERN_MAP.put(Pattern.compile("\"" + es.getKey() + "\"", Pattern.CASE_INSENSITIVE),
                        "\"" + es.getValue() + "\"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String format() {

        String body = StringUtils.defaultString(getBodyParams(), "");
        if (body.indexOf("{") < 0) {
            body = "{}";
        }

        ResultStatus<String> res = new ResultStatus<>();
        String result = JsonUtils.toStandardJson(body);

        for (Map.Entry<Pattern, String> es : REPLACE_PATTERN_MAP.entrySet()) {
            result = es.getKey().matcher(result).replaceAll(es.getValue());
        }

        TypeReference<HashMap<String, Object>> t = new TypeReference<HashMap<String, Object>>() {

        };
        Map<String, Object> dataMap = JsonUtils.fromJson(result, t);
        dataMap.putIfAbsent("name", "");
        dataMap.putIfAbsent("url", "");
        dataMap.putIfAbsent("method", "GET");
        dataMap.putIfAbsent("headers", new HashMap<String, String>());
        dataMap.putIfAbsent("body", "");

        if (StringUtils.isBlank(dataMap.getOrDefault("name", "") + "")) {
            dataMap.put("name", UrlUtils.getPath(dataMap.getOrDefault("url", "") + ""));
        }

        if (dataMap.get("headers") instanceof String) {
            dataMap.put("headers", new HashMap<String, String>());
        }
        result = JsonUtils.toJson(dataMap);

        return JsonUtils.toJson(res.ofData(result));
    }

    public String version() {
        ResultStatus<String> res = new ResultStatus<>();
        return JsonUtils.toJson(res.of(Msg.OK, "1"));
    }

    public String version2() {
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ResultStatus<String> res = new ResultStatus<>();
        return JsonUtils.toJson(res.of(Msg.OK, "1"));
    }

    public String version3() {
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultStatus<String> res = new ResultStatus<>();
        return JsonUtils.toJson(res.of(Msg.OK, "1"));
    }

    public String add() throws IOException {

        ReqInfo ri = getBodyParams(ReqInfo.class);

        ReqAccount acc = currentAccount();

        ResultStatus<ReqInfo> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        requestInfoService.add(ri, acc);
        res.ofData(ri);

        return JsonUtils.toJson(res.of(Msg.OK));

    }

    public String update() throws IOException {

        // RequestInfo ri = requestInfoService.findOne(id);
        ReqInfo ri = getBodyParams(ReqInfo.class);
        checkNotNull(ri);
        ReqAccount acc = currentAccount();

        Map<String, String> meta = ri.getMeta();
        String requestData = meta.get("request");

        JSONObject reqJsonObject = new JSONObject(requestData);

        String method = JsonUtils.val(reqJsonObject, "method", "GET");
        String reqUrl = reqJsonObject.get("url") + "";

        ResultStatus<ReqInfo> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();

        ri.setMethod(HttpMethod.valueOf(StringUtils.upperCase(method)));
        ri.setUrl(reqUrl);

        if (ri.getId() != null) {
            ReqInfo ri1 = requestInfoService.findOne(ri.getId());
            if (ri1 != null) {
                ri.setCreatedOn(ri1.getCreatedOn());
            }
        }
        ri.setUpdatedOn(new Date());
        requestInfoService.update(ri, acc);
        res.setData(ri);

        return JsonUtils.toJson(res);
    }

    public String importType() {
        ResultStatus<List<String>> res = new ResultStatus<>();
        res.setData(IMPORT_TYPE);
        return JsonUtils.toJson(res);
    }

    public String exportType() {
        ResultStatus<List<String>> res = new ResultStatus<>();
        res.setData(EXPORT_TYPE);
        return JsonUtils.toJson(res);
    }

    public String importRequest() {
        TypeValuePair ri = getBodyParams(TypeValuePair.class);
        MyRequest data = new ConverterBuilder().build(ri.getType()).toMyRequest(ri.getValue());

        ResultStatus<String> res = new ResultStatus<>();
        res.setData(JsonUtils.toJson(data));
        return JsonUtils.toJson(res);
    }

    public String exportRequest() {
        String type = getUrlParam("type");
        MyRequest myRequest = getBodyParams(MyRequest.class);

        String data = new ConverterBuilder().build(type)
                .fromMyRequest(myRequest);

        ResultStatus<String> res = new ResultStatus<>();
        res.setData(data);
        return JsonUtils.toJson(res);
    }

    //@ApiOperation(value="测试接口", notes="测试接口详细描述")
    public String send() throws IOException, UnirestException {

        ReqInfo ri = getBodyParams(ReqInfo.class);
        String requestMeta = ri.getMeta().getOrDefault("request", "{}");
        HashMap map = JsonUtils.fromJson(requestMeta, HashMap.class);
        ri.setUrl(map.getOrDefault("url", "").toString());
        Preconditions.checkArgument(StringUtils.isNotBlank(ri.getUrl()), "Url should not be empty");

        ReqAccount acc = currentAccount();
        ResultStatus<UnirestRes<String>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();

        // RequestInfo ri = requestInfoService.findOne(id);
        UnirestRes<String> result = requestInfoService.send(ri, acc);

        res.setData(result);

        return JsonUtils.toJson(res);
    }

    public String list() throws IOException {
        System.out.println(" ====> list");

        ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();

        List<ReqGroup> ll = requestInfoService.listRequestInfoByAccount(acc);

        res.setData(ll);

        return JsonUtils.toJson(res);
    }

    public String tree() throws IOException {
        System.out.println(" ====> tree");

        ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        List<ReqGroup> ll = requestInfoService.listRequestInfoByAccount(acc);

        res.setData(ll);

        return JsonUtils.toJson(res);

    }

    //RequestMapping( value = "/list/swagger/data", method = RequestMethod.GET)
    public String listBySwaggerId() throws IOException {
        System.out.println(" ====> ");

        String swaggerId = getUrlParam("swaggerId");
        checkNotNull(swaggerId);

        ReqAccount acc = currentAccount();
        ResultStatus<List<ReqInfo>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        List<ReqInfo> ll = requestInfoService.listRequestInfoBySwaggerId(acc, swaggerId);

        res.setData(ll);
        return JsonUtils.toJson(res);
    }

    //RequestMapping( value = "/detail/swagger/data", method = RequestMethod.GET)
    public String detail2() throws IOException {
        System.out.println(" ====> ");
        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id);

        ReqAccount acc = currentAccount();
        ResultStatus<ReqInfo> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        ReqInfo ri = requestInfoService.detail(id, acc);

        res.setData(ri);

        return JsonUtils.toJson(res);
    }

    public String detail() throws IOException {
        System.out.println(" ====> ");

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id);

        ReqAccount acc = currentAccount();
        ResultStatus<ReqInfo> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        ReqInfo ri = requestInfoService.detail(id, acc);

        res.setData(ri);

        return JsonUtils.toJson(res);

    }

    //RequestMapping( value = "/methods", method = RequestMethod.GET)
    public String methods() {
        System.out.println(" ====> methods");
        ResultStatus<HttpMethod[]> res = new ResultStatus<>();

        HttpMethod[] values = HttpMethod.values();

        return JsonUtils.toJson(res.ofData(values));
    }

    //RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete() throws IOException {

        Long id = Long.parseLong(getUrlParam("id"));

        //ReqAccount acc = currentAccount();
        ResultStatus<ReqInfo> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        //requestInfoService.findOne(id);
        //todo check userid
        requestInfoService.deleteByPrimaryKey(id);

        return JsonUtils.toJson(res);

    }

    //RequestMapping(value = "/group/list" , method = RequestMethod.GET)
    public String groupList() throws IOException {
        System.out.println(" ====> ");

        ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqGroupService reqGroupService = new ReqGroupServiceImpl();

        List<ReqGroup> ll = reqGroupService.listByAccount(acc);

        res.setData(ll);

        return JsonUtils.toJson(res);
    }

    public String groupAdd() throws IOException {

        ReqGroup g = getBodyParams(ReqGroup.class);
        checkNotNull(g);

        //ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqGroupService reqGroupService = new ReqGroupServiceImpl();
        reqGroupService.add(g);

        return JsonUtils.toJson(res);
    }

    //RequestMapping(value = "/group/update", method = RequestMethod.POST)
    public String groupUpdate() throws IOException {

        ReqGroup g = getBodyParams(ReqGroup.class);
        checkNotNull(g);

        //ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqGroupService reqGroupService = new ReqGroupServiceImpl();

        ReqGroup reqGroup = reqGroupService.findOne(g.getGroupId());
        reqGroup.setName(g.getName());
        reqGroupService.save(reqGroup);

        return JsonUtils.toJson(res);
    }

    //@RequestMapping(value = "/group/delete", method = RequestMethod.POST)
    public String groupDelete() throws IOException {

        Long id = Long.parseLong(getUrlParam("id"));

        //ReqGroup g = getBodyParams(ReqGroup.class);

        //ReqAccount acc = currentAccount();
        ResultStatus<List<ReqGroup>> res = new ResultStatus<>();

        ReqGroupService reqGroupService = new ReqGroupServiceImpl();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();

        ReqGroup rg = reqGroupService.findOne(id);
        List<ReqInfo> reqs = requestInfoService.listRequestInfoByReqGroup(rg);
        if (!reqs.isEmpty()) {
            return JsonUtils.toJson(res.of(Msg.NOT_ALLOWED));
        }
        reqGroupService.delete(rg);

        return JsonUtils.toJson(res);

    }
}
