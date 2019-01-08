package com.haleywang.monitor.dto;


import lombok.Data;

@Data
public class ResultStatus<T> {


	private T data;

	private ResponseMeta meta;


	public ResultStatus(T data) {
		super();
		this.data = data;
	}
	
	public ResultStatus() {
		meta = new ResponseMeta();
	}



	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}


	public ResultStatus of(String code) {
		meta.code = code;
		return this;
	}

	public ResultStatus of(String code, T result) {
		meta.code = code;
		this.data = result;
		return this;
	}

	public ResultStatus ofData(T result) {
		this.data = result;
		return this;
	}

	public ResultStatus ofCode(String s) {
		meta.setCode(s);
		return this;
	}

	public ResultStatus ofMsg(String msg) {
		meta.setMsg(msg);
		return this;
	}
}
