package com.haleywang.monitor.common.req;

import com.haleywang.monitor.dto.UnirestRes;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static okhttp3.internal.Util.UTF_8;

/**
 * @author haley
 * @date 2018/12/16
 */
public class HttpUtils {

	private HttpUtils() {
	}


	private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)
			//设置连接超时
			.readTimeout(120, TimeUnit.SECONDS)
			//设置读超时
			.writeTimeout(120, TimeUnit.SECONDS)
			//设置写超时
			.retryOnConnectionFailure(true)
			//是否自动重连
			.build();


	public static UnirestRes send(HttpRequestItem reqItem) throws IOException {

		String url = reqItem.getUrl();
		HttpMethod httpMethod = reqItem.getHttpMethod();
		Map<String, String> reqHeaders = reqItem.getReqHeaders();
		String bodyStr = reqItem.getDataBuff().toString();


		return send(url, httpMethod, reqHeaders, bodyStr);

	}

	public static UnirestRes get(String url) throws IOException {
		return send(url, HttpMethod.GET, null, null);
	}

	private static UnirestRes send(String url, HttpMethod httpMethod, Map<String, String> reqHeaders1, String bodyStr) throws IOException {
		Map<String, String> reqHeaders = reqHeaders1 != null ? reqHeaders1 : new HashMap<>();
		String contentType = reqHeaders.getOrDefault(reqHeaders.get("content-type"), reqHeaders.getOrDefault("Content-Type", "text/plain"));
		MediaType mediaType = MediaType.parse(contentType);
		RequestBody body = okhttp3.internal.http.HttpMethod.permitsRequestBody(httpMethod.name()) ? RequestBody.create(mediaType, bodyStr) : null;

		Request request = new Request.Builder().url(url)
				.method(httpMethod.name(), body)
				.headers(Headers.of(reqHeaders))
				.build();
		Response response = CLIENT.newCall(request).execute();

		Charset charset = Util.bomAwareCharset(response.body().source(), charset(response.body()));

		try (InputStream in = response.body().byteStream()) {
			// fix chunked response
			List<String> lines = IOUtils.readLines(in, charset);
			return new UnirestRes().withRes(response, StringUtils.join(lines, "\n"));
		}
	}

	private static Charset charset(ResponseBody rb) {
		MediaType contentType = rb.contentType();
		return contentType != null ? contentType.charset(UTF_8) : UTF_8;
	}

}
