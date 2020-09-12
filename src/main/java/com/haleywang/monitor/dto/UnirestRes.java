package com.haleywang.monitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haleywang.monitor.common.Constants;
import com.haleywang.monitor.utils.JsonUtils;
import lombok.Data;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haley
 * @date 2018/12/16
 */
@Data
public class UnirestRes {

	@JsonIgnore
	Response res;

	String testResult;

	Boolean testSuccess;

	private long begin;
	private long end;
	private String body;

	public static UnirestRes newInstance() {
		return new UnirestRes();
	}


	public UnirestRes withRes(Response res) throws IOException {
		this.res = res;
		body = res.body().string();
		return this;
	}

	public int getStatus() {
		if(res == null) {
			return 0;
		}
		return res.code();
	}

	public String getStatusText() {
		return getStatus()+"";
	}

	public Boolean getTestSuccess() {
		return testSuccess;
	}

	public void setTestSuccess(Boolean testSuccess) {
		this.testSuccess = testSuccess;
	}

	/**
	 * @return Response Headers (map) with <b>same case</b> as server response.
	 * For instance use <code>getHeaders().getFirst("Location")</code> and not <code>getHeaders().getFirst("location")</code> to get first header "Location"
	 */
	public String getHeaders() {
		if (res == null) {
			return null;
		}
		return JsonUtils.toJson(res.headers().toMultimap());
	}


	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>(Constants.DEFAULT_MAP_SIZE);
		map.put("statusCode", getStatus());
		map.put("body", body);
		return map;
	}

	public String getBody() {
		return body;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}


	public long getSpentTime() {
		return end - begin;
	}

}
