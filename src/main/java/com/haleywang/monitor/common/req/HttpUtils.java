package com.haleywang.monitor.common.req;

import com.haleywang.monitor.dto.UnirestRes;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

	private HttpUtils(){}


	private static final OkHttpClient CLIENT =new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
			.readTimeout(120, TimeUnit.SECONDS)          //设置读超时
			.writeTimeout(120,TimeUnit.SECONDS)          //设置写超时
			.retryOnConnectionFailure(true)             //是否自动重连
			.build();                                   //构建OkHttpClient对象



	public static UnirestRes send(HttpRequestItem reqItem) throws IOException {

		String url = reqItem.getUrl();
		HttpMethod httpMethod = reqItem.getHttpMethod();
		Map<String, String> reqHeaders = reqItem.getReqHeaders();
		String bodyStr = reqItem.getDataBuff().toString();


		String contentType = reqHeaders.getOrDefault(reqHeaders.get("content-type"), reqHeaders.getOrDefault("Content-Type", "text/plain"));
		MediaType mediaType = MediaType.parse(contentType);
		RequestBody body = okhttp3.internal.http.HttpMethod.permitsRequestBody(httpMethod.name()) ? RequestBody.create(mediaType, bodyStr) : null;

		Request request = new Request.Builder().url(url)
				.method(httpMethod.name(), body)
				.headers(Headers.of(reqHeaders))
				.build();
		Response response = CLIENT.newCall(request).execute();

		return new UnirestRes().withRes(response);

	}

}
