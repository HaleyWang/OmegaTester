package com.haleywang.monitor.common.login;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.entity.ReqAccount;
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
		if(loginCookieOuterEncryptVal == null) {
			return null;
		}
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
		if(acc == null || loginCookieOuterEncryptVal == null){
			return null;
		}

		if (acc.getAccountId() == null) {
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
