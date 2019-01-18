package com.haleywang.monitor.dto;

import java.util.List;

import com.haleywang.monitor.common.req.HttpMethod;

public class RequestData {
	private String url;
	private HttpMethod method;
	private List<Header> header;
	private Body body;
	private String description;
	
	
	public static class Header {
		private String key;
		private String value;
		private String description;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	
	public static enum BodyMode {
		RAW, FORM_DATA, URLENCODED
	}
	
	public static class Body {
		private BodyMode mode; //raw, formdata, urlencoded
		private String formdata;
		private String raw;
		public BodyMode getMode() {
			return mode;
		}
		public void setMode(BodyMode mode) {
			this.mode = mode;
		}
		public String getFormdata() {
			return formdata;
		}
		public void setFormdata(String formdata) {
			this.formdata = formdata;
		}
		public String getRaw() {
			return raw;
		}
		public void setRaw(String raw) {
			this.raw = raw;
		}
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public List<Header> getHeader() {
		return header;
	}

	public void setHeader(List<Header> header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
