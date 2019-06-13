package com.haleywang.monitor.common.req.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.haleywang.monitor.dto.MyRequest;

@MyRequestExportAnnotation(name = "cURL")
@MyRequestImportAnnotation(name = "cURL")
public class CurlConverter implements ReqConverter {

    @Override
    public MyRequest toMyRequest(TypeValuePair pair) {
        String input = pair.getValue();

        Preconditions.checkNotNull(input, "input should not be null");

        MyRequest req = new MyRequest();
        Map<String, String> headers = new HashMap<>();
        req.setHeaders(headers);

        List<String> res = Splitter.onPattern("\\s+(?='|-)").splitToList(input);
        for(int i = 0, n = res.size() ; i < n-1; i++) {
            String curlItem = res.get(i);
            String curlItemNext = res.get(i + 1);
            curlItemNext = StringUtils.stripStart(curlItemNext, "'");
            curlItemNext = StringUtils.stripEnd(curlItemNext, "'");

            if(curlItem.equals("-X")) {
                List<String> kv = Splitter.onPattern(":\\s+").splitToList(curlItem);
                if(kv.size() == 2) {
                    req.setMethod(HttpMethod.parse(kv.get(1)));
                    continue;
                }
            }

            if(curlItem.equals("-H")) {
                List<String> kv = Splitter.onPattern(":\\s+").splitToList(curlItemNext);
                if(kv.size() == 2) {
                    headers.put(kv.get(0), kv.get(1));
                    continue;
                }
            }

            if(curlItem.startsWith("'https:") || curlItem.startsWith("'http:")) {
                curlItem = StringUtils.stripStart(curlItem, "'");
                curlItem = StringUtils.stripEnd(curlItem, "'");

                req.setUrl(curlItem);
                continue;
            }

            if(curlItem.equals("-X")) {
                req.setMethod(EnumUtils.getEnum(HttpMethod.class, StringUtils.upperCase(curlItemNext)));
                continue;
            }

            if(curlItem.equals("-d")) {
                req.setBody(curlItemNext);
                if(req.getMethod() == null) {
                    req.setMethod(HttpMethod.POST);
                }
                continue;
            }

            if(curlItem.equals("--data-binary")) {
                req.setBody(curlItemNext);
                if(req.getMethod() == null) {
                    req.setMethod(HttpMethod.POST);
                }
                continue;
            }

        }
        return req;
    }

    @Override
    public String fromMyRequest(TypeValuePair pair) {

        String reqString = pair.getValue();
        MyRequest myRequest = JsonUtils.fromJson(reqString, MyRequest.class);

        StringBuffer sb = new StringBuffer();
        sb.append("curl -X "+ myRequest.getMethod() +" \\")
                .append(" '"+myRequest.getUrl()+"' \\");
        for(Map.Entry<String, String> entry : myRequest.getHeaders().entrySet()) {
            sb.append(" -H ").append("'").append(entry.getKey()).append(": ").append(entry.getValue()).append(" \\");
        }

        if(StringUtils.isNotBlank(myRequest.getBody())) {
            sb.append(" -d ").append("'").append(myRequest.getBody()).append("'");
        }
        return sb.toString();
    }
}
