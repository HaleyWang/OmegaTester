package com.haleywang.monitor.dto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haleywang.monitor.utils.JsonUtils;
import lombok.Data;
import okhttp3.Response;

@Data
public class UnirestRes  {

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
		if(res == null) {
			return null;
		}
		return JsonUtils.toJson(res.headers().toMultimap());
	}


	public Map<String, Object> toMap ()  {
		Map<String, Object> map = new HashMap<>();

		map.put("statusCode" , getStatus());
		map.put("body" , body);

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


	public long getSpent_time() {
		return end - begin;
	}

}
