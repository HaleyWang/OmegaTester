package com.haleywang.monitor.dto;


import lombok.Data;

/**
 * @author haley
 */
@Data
public class ResultMessage<T, M extends Message> {


	private T data;
	private M code;
	private String msg;

	private ResponseMeta meta;

	public ResultMessage() {
		super();
		meta = new ResponseMeta();
	}

	public ResultMessage(T data) {
		super();
		meta = new ResponseMeta();
		this.data = data;
	}

	public ResultMessage(M message, T data) {
		super();
		meta = new ResponseMeta();
		this.data = data;
		this.code = message;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResultMessage ofData(T result) {
		this.data = result;
		return this;
	}

	public ResultMessage ofMessage(M s) {
		this.code = s;
		return this;
	}

	public String getMessage() {
		if(this.code == null) {
			return null;
		}
		return this.code.getDescription();
	}

}
