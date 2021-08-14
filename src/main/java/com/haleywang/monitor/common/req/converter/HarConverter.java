package com.haleywang.monitor.common.req.converter;

import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.dto.Har;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.NameValue;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haley
 * @date 2018/12/16
 */
@MyRequestImportAnnotation(name = "HAR")
public class HarConverter implements ReqConverter {

    @Override
    public String fromMyRequest(TypeValuePair t) {
        MyRequest myRequest = JsonUtils.fromJson(t.getValue(), MyRequest.class);
        Har har = new Har();
        List<Har.HarEntry> entries = new ArrayList<>();
        har.setLog(new Har.HarLog());
        har.getLog().setEntries(entries);

        Optional.ofNullable(myRequest).map(o -> {
            Har.HarEntry entry = new Har.HarEntry();
            Har.HarRequest req = new Har.HarRequest();
            req.setUrl(myRequest.getUrl());
            req.setMethod(Optional.ofNullable(myRequest.getMethod()).map(Enum::name).orElse(null));
            Map<String, String> myRequestHeaders = myRequest.getHeaders();
            List<NameValue> headers = toNameValues(myRequestHeaders);
            req.setHeaders(headers);
            entry.setRequest(req);
            return entry;
        }).ifPresent(entries::add);

        return JsonUtils.toJson(har);
    }

    private List<NameValue> toNameValues(Map<String, String> myRequestHeaders) {
        return Optional.ofNullable(myRequestHeaders).orElse(new HashMap<>(Constants.DEFAULT_MAP_SIZE)).entrySet()
                .stream().map(it -> {
                    NameValue nameValue = new NameValue();
                    nameValue.setName(it.getKey());
                    nameValue.setValue(it.getValue());
                    return nameValue;
                }).collect(Collectors.toList());
    }

    public List<MyRequest> toMyRequests(Har har) {

        List<MyRequest> list = new ArrayList<>();
        List<Har.HarEntry> entries = Optional.ofNullable(har).map(Har::getLog).map(Har.HarLog::getEntries).orElse(new ArrayList<>());
        for(Har.HarEntry entry : entries) {
            MyRequest myRequest = new MyRequest();
            Har.HarRequest harReq = entry.getRequest();
            myRequest.setUrl(harReq.getUrl());
            //myRequest.setBody(harReq.get)
            //TODO body
            myRequest.setMethod(EnumUtils.getEnum(HttpMethod.class, StringUtils.upperCase(harReq.getMethod())));
            List<NameValue> nameValueHeaders = harReq.getHeaders();
            Map<String, String> headers = toStringStringMap(nameValueHeaders);
            myRequest.setHeaders(headers);
            list.add(myRequest);
        }
        return list;
    }

    private Map<String, String> toStringStringMap(List<NameValue> nameValueHeaders) {
        return Optional.ofNullable(nameValueHeaders).orElse(new ArrayList<>()).stream().collect(
                Collectors.toMap(NameValue::getName, NameValue::getValue, (r, s) -> s));
    }

    @Override
    public MyRequest toMyRequest(TypeValuePair o) {
        String har = o.getValue();

        return toMyRequests(JsonUtils.fromJson(har, Har.class)).stream().findFirst().orElse(null);
    }

}
