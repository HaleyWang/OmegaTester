package com.haleywang.monitor.common.login;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.utils.AesUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author haley
 * @date 2018/12/16
 */
public class LoginCookieDecrypt {

	private String loginCookieOuterEncryptVal;

	private Long accountId;

	public LoginCookieDecrypt(String loginCookieOuterEncryptVal) {
		super();
		this.loginCookieOuterEncryptVal = loginCookieOuterEncryptVal;
	}

	public Long getAccountId() {
		if (StringUtils.isBlank(loginCookieOuterEncryptVal)) {
			return null;
		}
		try {
			String str = AesUtil.decrypt(loginCookieOuterEncryptVal, AesUtil.ENCRYPTION_COMM_KEY);

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
			String str = AesUtil.decrypt(loginCookieOuterEncryptVal, acc.getAkey());

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
