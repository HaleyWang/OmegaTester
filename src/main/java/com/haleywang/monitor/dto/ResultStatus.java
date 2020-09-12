package com.haleywang.monitor.dto;


import lombok.Data;

@Data
public class ResultStatus<T> extends ResultMessage<T, Message> {

	public ResultStatus() {
		super();
	}

	public ResultStatus(T data) {
		super(data);
	}

	public ResultStatus(Message message, T data) {
		super(message, data);
	}


	public ResultStatus withData(T result) {
		ofData(result);
		return this;
	}
}
