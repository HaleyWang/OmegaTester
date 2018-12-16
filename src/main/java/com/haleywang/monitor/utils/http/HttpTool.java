package com.haleywang.monitor.utils.http;

import java.net.MalformedURLException;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.utils.JsonUtils;
import com.mashape.unirest.http.exceptions.UnirestException;

public class HttpTool {

    public String send(String preReqData, String envString) {

        try {
            HttpRequestItem reqItem = ReqInfoHelper.getHttpRequestItem(preReqData, envString, null);

            UnirestRes<String> result = new UnirestRes<String>(
                    HttpUtils.send(reqItem));

            return JsonUtils.toJson(result);

        } catch (MalformedURLException e) {
            throw new ReqException(e.getMessage(), e);
        } catch (UnirestException e) {
            throw new ReqException(e.getMessage(), e);
        }
    }
}
