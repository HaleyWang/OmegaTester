package com.haleywang.monitor.common.req;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.TemplateUtils;
import com.haleywang.monitor.utils.UrlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ReqInfoHelper {

	private ReqInfoHelper(){}

	public static final String HEADERS = "headers";
	public static final String DIAGONAL = "/";

	public static HttpRequestItem parse(ReqInfo ri, ReqSetting envStrring, String preReqResultStr)
			throws MalformedURLException {

		Map<String, String>  meta = ri.getMeta();
		String request = meta.getOrDefault("requestJson", meta.get("request"));
		Preconditions.checkNotNull(request, "request should not be null, reqInfo id:" + ri.getId());


		String envJson = envStrring != null ? envStrring.getContent() : "";

		int caseIndex = ri.getCaseIndex();

		String casesStr = meta.getOrDefault("casesJson", meta.get("cases"));


		return getHttpRequestItem(request, envJson, casesStr,  caseIndex, preReqResultStr);
	}

	public static HttpRequestItem getHttpRequestItem(String request, String envJson,String casesStr, int caseIndex, String preReqResultStr) throws MalformedURLException {

		Preconditions.checkNotNull(request, "request should not be null");

		HashMap<String, Objects> map = new HashMap<>();
		if(StringUtils.isNotBlank(envJson)) {

			TypeReference<HashMap> t = new TypeReference<HashMap>() {
			};

			HashMap<String, Objects> envMap = JsonUtils.fromJson(envJson, t);
            if(envMap != null) {
                map.putAll(envMap);
            }
		}

		if(StringUtils.isNotBlank(casesStr)) {

			TypeReference<HashMap> t = new TypeReference<HashMap>() {
			};

			HashMap<String, Object> caseDataMaps = JsonUtils.fromJson(casesStr, t);

			Object obj = Optional.ofNullable(caseDataMaps).orElse(new HashMap<>()).get(caseIndex+"");
			if(obj instanceof Map) {
				map.putAll((Map)obj);
			}
		}

		if(StringUtils.isNotBlank(preReqResultStr)) {

			TypeReference<HashMap> t = new TypeReference<HashMap>() {
			};

			HashMap<String, Objects> preReqResultMap = JsonUtils.fromJson(preReqResultStr, t);
			map.putAll(preReqResultMap);
		}


		if(map.size() > 0) {
			request = TemplateUtils.parse(request, map);
		}


		HttpRequestItem reqItem = new HttpRequestItem();
		JSONObject reqJsonObject = new JSONObject(request);

		Preconditions.checkArgument(StringUtils.isNotEmpty(reqJsonObject.getString("url")), "request url should not be null");


		String method = StringUtils.defaultIfBlank(ObjectUtils.toString(JsonUtils.val(reqJsonObject, "method")), "GET");
		reqItem.setHttpMethod(HttpMethod.valueOf(StringUtils.upperCase(method.toUpperCase())));

		String qs = getQs(reqJsonObject);

		String[] urlArr = splitUrl(reqJsonObject.get("url")+"");
		reqItem.setHost(urlArr[0]);
		String path = urlArr[1];
		if(StringUtils.isNotBlank(qs)) {
			path += "?" + qs;
		}
		reqItem.setPath(path);

		fillReqItemHeader(reqJsonObject, reqItem);

		fillReqItemBody(reqJsonObject, reqItem);

		return reqItem;
	}

	private static String getQs(JSONObject reqJsonObject) {
		if(!reqJsonObject.has("qs")) {
			return "";
		}
		Object qs = reqJsonObject.get("qs");
		StringBuilder buff = new StringBuilder();
		if(qs instanceof JSONObject) {
			JSONObject qsJson = (JSONObject)qs;
			Set set = qsJson.keySet();
			int i = 1;
			int n = set.size();
			for(Object obj : set) {
				String key = obj+"";
				buff.append(key).append("=").append(qsJson.get(key));
				if(i != n) {
					buff.append("&");
				}
				i++;
			}
		}
		return buff.toString();
	}

	private static void fillReqItemBody(JSONObject ri, HttpRequestItem reqItem) {


		JSONObject form = null;

		if(ri.has("form") ) {
			form = (JSONObject) ri.get("form");
		}


		StringBuilder sb = new StringBuilder();
		String d = "";
		if(form != null) {
			Set ks = form.keySet();
			int i = 0;
			int n = ks.size();
			for (Object key : ks) {
				
				String k = key.toString();
				String val = UrlUtils.encode(form.get(k)+"");
				sb.append(k).append("=").append(val);
				if (i != n - 1) {
					sb.append("&");
				}
				i++;
			}
			d = sb.toString();
		}else if(ri.has("body")) {
			Object body = ri.get("body");
			if(body instanceof JSONObject) {
				JSONObject bodyJson = (JSONObject) body;
				d = bodyJson.toString();
			}else {
				d = body.toString();
			}
		}
		reqItem.appendData(d);
	}

	private static void fillReqItemHeader(JSONObject ri, HttpRequestItem reqItem) {


		if(!ri.has(HEADERS) || ri.get(HEADERS) == null) {
			return;
		}

		JSONObject headers = (JSONObject) ri.get(HEADERS);
		
		for(Object headKey : headers.keySet()) {
			reqItem.addReqHeader(headKey + "",
					headers.get(headKey+"") + "");
		}
	}

	private static String[] splitUrl(String str) throws MalformedURLException {
		URL url = new URL(str);

		String subUrl = str.substring(str.indexOf(url.getHost()));

		String host = str.substring(0, str.indexOf(url.getHost()))
				+ subUrl.substring(0, subUrl.indexOf(DIAGONAL));
		String path = subUrl.substring(subUrl.indexOf(DIAGONAL));

		return new String[] { host, path };
	}

}
