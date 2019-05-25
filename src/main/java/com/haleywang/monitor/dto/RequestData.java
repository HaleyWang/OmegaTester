package com.haleywang.monitor.dto;

import java.util.List;

import com.haleywang.monitor.common.req.HttpMethod;
import lombok.Data;

@Data
public class RequestData {
	private String url;
	private HttpMethod method;
	private List<Header> header;
	private Body body;
	private String description;

	@Data
	public static class Header {
		private String key;
		private String value;
		private String description;
	}
	
	public static enum BodyMode {
		RAW, FORM_DATA, URLENCODED
	}

	@Data
	public static class Body {
		private BodyMode mode; //raw, formdata, urlencoded
		private String formdata;
		private String raw;

	}

}
