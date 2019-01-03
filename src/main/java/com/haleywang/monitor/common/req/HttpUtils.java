package com.haleywang.monitor.common.req;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpUtils {


	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
	

	public static HttpResponse<String> send(HttpRequestItem reqItem) throws UnirestException {

		String url = reqItem.getUrl();
		HttpMethod httpMethod = reqItem.getHttpMethod();
		Map<String, String> reqHeaders = reqItem.getReqHeaders();
		String body = reqItem.getDataBuff().toString();

		//url= TemplateUtils.parse(url, data);
		//body= TemplateUtils.parse(body, data);

		/*
		Map<String, String> newReqHeaders = new HashMap<>();
		for(String key : reqHeaders.keySet()) {
			String newKey = TemplateUtils.parse(key, data);
			newReqHeaders.put(newKey , TemplateUtils.parse(reqHeaders.get(key), data));
		}
		*/

		HttpResponse<String> vvv = new HttpRequestWithBody(httpMethod, url).
				headers(reqHeaders).body(body).asString();
		return vvv;
	}

}
