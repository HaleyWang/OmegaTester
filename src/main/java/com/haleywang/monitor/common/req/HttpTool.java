package com.haleywang.monitor.common.req;

import java.io.IOException;
import java.net.MalformedURLException;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.utils.JsonUtils;

public class HttpTool {

    public String send(String preReqData, String envString) throws IOException {

        try {
            HttpRequestItem reqItem = ReqInfoHelper.getHttpRequestItem(preReqData, envString, null,0,null);

            UnirestRes result = HttpUtils.send(reqItem);

            return JsonUtils.toJson(result);

        } catch (Exception e) {
            throw new ReqException(e.getMessage(), e);
        }
    }
}
