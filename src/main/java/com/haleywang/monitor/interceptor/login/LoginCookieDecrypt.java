package com.haleywang.monitor.interceptor.login;

import java.util.Date;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.utils.AESUtil;

public class LoginCookieDecrypt {

	private String loginCookieOuterEncryptVal;

	//private String loginCookieInnerEncryptVal;

	private Long accountId;

	public LoginCookieDecrypt(String loginCookieOuterEncryptVal) {
		super();
		this.loginCookieOuterEncryptVal = loginCookieOuterEncryptVal;
	}

	public Long getAccountId() {
		try {
			String str = AESUtil.decrypt(loginCookieOuterEncryptVal, AESUtil.ENCRYPTION_COMM_KEY);
	
			String[] arr = str.split("@@");
	
			accountId = Long.parseLong(arr[0]);
			loginCookieOuterEncryptVal = arr[1];
	
			return accountId;
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}

	}

	public String getEmail(ReqAccount acc) {
		if(acc == null){
			return null;
		}
		try {
			String str = AESUtil.decrypt(loginCookieOuterEncryptVal, acc.getAkey());

			String[] arr = str.split("@@");

			String email = arr[0];
			if (!acc.getEmail().equals(email)) {
				email = null;
			}
			return email;
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);

		}
	}

}
