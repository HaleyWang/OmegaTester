package com.haleywang.monitor.common;
public class ReqIllegalArgumentException extends ReqException {
	
	private final String code;
	private final String fieldName;


    public ReqIllegalArgumentException(String code, String fieldName, String message) {
        super(message);
		this.code = code;
		this.fieldName = fieldName;
	}

	public static ReqIllegalArgumentException ofPostBodyError() {

    	return new ReqIllegalArgumentException("POST_BODY_ERROR" , "body", "error");
	}


	public String getCode() {
		return code;
	}


	public String getFieldName() {
		return fieldName;
	}


}