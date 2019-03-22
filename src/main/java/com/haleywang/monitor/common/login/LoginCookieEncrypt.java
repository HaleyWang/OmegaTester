package com.haleywang.monitor.common.login;

import java.util.Date;

import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.utils.AESUtil;

public class LoginCookieEncrypt {

	private ReqAccount acc;

	public LoginCookieEncrypt(ReqAccount acc) {
		super();
		this.acc = acc;
	}

	public String genetrateLoginCookieEncryptVal() throws Exception {
		String loginCookieOriginalVal = acc.getEmail() + "@@" + (new Date()).getTime();
		String loginCookieInnerEncryptVal = AESUtil.encrypt(loginCookieOriginalVal, acc.getAkey());

		String loginCookieOuterEncryptVal = AESUtil.encrypt(acc.getAccountId() + "@@" + loginCookieInnerEncryptVal,
				AESUtil.ENCRYPTION_COMM_KEY);

		return loginCookieOuterEncryptVal;
		
	}

}
