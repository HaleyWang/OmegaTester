package com.haleywang.monitor.dto;

public class ResultStatus<T> {
	
	
	
	public ResultStatus(T data) {
		super();
		this.data = data;
	}
	
	public ResultStatus() {}


	String code;
	String msg;
	T data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}


	public ResultStatus of(String code) {
		this.code = code;
		return this;
	}

	public ResultStatus of(String code, T result) {
		this.code = code;
		this.data = result;
		return this;
	}

	public ResultStatus ofData(T result) {
		this.data = result;
		return this;
	}
}
