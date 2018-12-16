package com.haleywang.monitor.common;
public class ValidException extends ReqException {
	
	int code;
	String fieldName;
	
	

    /**
     * 
     */
    private static final long serialVersionUID = -9086697309592524885L;

    public ValidException() {
        super();
    }
    
    

    public ValidException(int code, String fieldName, String message) {
        super(message);
		this.code = code;
		this.fieldName = fieldName;
	}



	public ValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidException(String message) {
        super(message);
    }

    public ValidException(Throwable cause) {
        super(cause);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

    
}