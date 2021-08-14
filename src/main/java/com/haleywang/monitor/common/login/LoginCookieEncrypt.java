package com.haleywang.monitor.common.login;

import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.utils.AesUtil;

import java.util.Date;

/**
 * @author haley
 * @date 2018/12/16
 */
public class LoginCookieEncrypt {

	private ReqAccount acc;

	public LoginCookieEncrypt(ReqAccount acc) {
		super();
		this.acc = acc;
	}

	public String genetrateLoginCookieEncryptVal() throws Exception {
		String loginCookieOriginalVal = acc.getEmail() + "@@" + (new Date()).getTime();
		String loginCookieInnerEncryptVal = AesUtil.encrypt(loginCookieOriginalVal, acc.getAkey());

		return AesUtil.encrypt(acc.getAccountId() + "@@" + loginCookieInnerEncryptVal,
				AesUtil.ENCRYPTION_COMM_KEY);

	}

}
