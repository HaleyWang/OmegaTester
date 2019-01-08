package com.haleywang.monitor.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haleywang.monitor.utils.JsonUtils;
import com.mashape.unirest.http.HttpResponse;
import lombok.Data;

@Data
public class UnirestRes<T>  {

	@JsonIgnore
	HttpResponse<T> res;

	String testResult;
	
	Boolean testSuccess;

	private long begin;
	private long end;


	public UnirestRes(HttpResponse<T> res) {
		super();
		this.res = res;
	}
	
	public int getStatus() {
		return res.getStatus();
	}

	public String getStatusText() {
		return res.getStatusText();
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
		//return FormatUtil.formatJson(JsonUtils.toJson(res.getHeaders()));
		return JsonUtils.toJson(res.getHeaders());
	}


	public Map<String, Object> toMap () {
		Map<String, Object> map = new HashMap<>();

		map.put("statusCode" , res.getStatus());
		map.put("statusCode" , res.getBody());

		return map;
    }

	public T getBody() {
		return res.getBody();
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
