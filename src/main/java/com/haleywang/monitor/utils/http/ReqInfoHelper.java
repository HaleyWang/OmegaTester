package com.haleywang.monitor.utils.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.model.ReqMeta;
import com.haleywang.monitor.model.ReqMeta.DataType;
import com.haleywang.monitor.model.ReqSetting;
import com.haleywang.monitor.utils.JsonUtils;

import com.haleywang.monitor.utils.TemplateUtils;
import com.haleywang.monitor.utils.UrlUtils;
import com.mashape.unirest.http.HttpMethod;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class ReqInfoHelper {

	public static HttpRequestItem parse(ReqInfo ri, ReqSetting envStrring, String preReqResultStr)
			throws MalformedURLException {

		String envJson = envStrring != null ? envStrring.getContent() : "";
		Map<String, String>  meta = ri.getMeta();

		String request = meta.get("request");

		return getHttpRequestItem(request, envJson, preReqResultStr);
	}

	public static HttpRequestItem getHttpRequestItem(String request, String envJson, String preReqResultStr) throws MalformedURLException {

		HashMap<String, Objects> map = new HashMap<>();
		if(StringUtils.isNotBlank(envJson)) {
			//HashMap<String, Objects> map = new HashMap<>();

			TypeReference<HashMap> t = new TypeReference<HashMap>() {
			};

			HashMap<String, Objects> envMap = JsonUtils.fromJson(envJson, t);
            if(envMap != null) {
                map.putAll(envMap);
            }
		}

		if(StringUtils.isNotBlank(preReqResultStr)) {
			//HashMap<String, Objects> map = new HashMap<>();

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
		StringBuffer buff = new StringBuffer();
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
		/*
		String subtab = ri.getMeta().get(ReqMeta.DataType.subtab.toString());
		String d = ri.getMeta().get(subtab);
		StringBuffer sb = new StringBuffer();
		if (d!= null && !ReqMeta.DataType.raw.equals(subtab)) {
			TypeReference<Map<String, String>> t = new TypeReference<Map<String, String>>() {
			};
			Map<String, String> map = JsonUtils.fromJson(d, t);
			String[] ks = map.keySet().toArray(new String[0]);
			for (int i = 0, n = ks.length; i < n; i++) {
				String k = ks[i];
				String val = URLEncoder.encode(map.get(k));
				sb.append(k).append("=").append(val);
				if (i != n - 1) {
					sb.append("&");
				}
			}
			d = sb.toString();
		}

		reqItem.appendData(d);
		*/


		JSONObject form = null;

		if(ri.has("form") ) {
			form = (JSONObject) ri.get("form");
		}


		StringBuffer sb = new StringBuffer();
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
		/*
		String headStr = ri.getMeta().get(ReqMeta.DataType.header.toString());
		if (headStr != null) {

			TypeReference<Map<String, String>> t = new TypeReference<Map<String, String>>() {
			};
			Map<String, String> headMap = JsonUtils.fromJson(headStr, t);
			Set<String> headKeys = headMap.keySet();
			for (String headKey : headKeys) {
				reqItem.addReqHeader(headKey, headMap.get(headKey));
			}
		}

		String subtab = ri.getMeta().get(ReqMeta.DataType.subtab.toString());
		if(subtab != null) {
			if (ReqMeta.DataType.urlencoded.equals(ReqMeta.DataType.valueOf(subtab) )) {
				reqItem.addReqHeader("content-type",
						"application/x-www-form-urlencoded");
			}
		}
		*/

		if(!ri.has("headers") || ri.get("headers") == null) {
			return;
		}

		JSONObject headers = (JSONObject) ri.get("headers");
		
		for(Object headKey : headers.keySet()) {
			reqItem.addReqHeader(headKey + "",
					headers.get(headKey+"") + "");
		}
	}

	private static String[] splitUrl(String str) throws MalformedURLException {
		URL url = new URL(str);
		System.out.println(url.getHost());
		System.out.println(str.indexOf(url.getHost()));

		System.out.println(str.substring(0, str.indexOf(url.getHost())));
		System.out.println(str.substring(str.indexOf(url.getHost())));

		String subUrl = str.substring(str.indexOf(url.getHost()));

		String host = str.substring(0, str.indexOf(url.getHost()))
				+ subUrl.substring(0, subUrl.indexOf("/"));
		String psth = subUrl.substring(subUrl.indexOf("/"));

		return new String[] { host, psth };
	}

}
