package com.haleywang.monitor.service.impl;

import java.util.List;
import java.util.UUID;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.utils.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.common.login.LoginCookieEncrypt;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.utils.AESUtil;
import com.haleywang.monitor.utils.Md5Utils;
import tk.mybatis.mapper.entity.Example;

public class ReqAccountServiceImpl extends BaseServiceImpl<ReqAccount> implements ReqAccountService {

	private ReqAccountRepository reqAccountRepository;

	public ReqAccountServiceImpl() {
		setReqAccountRepository();
	}

	public void setReqAccountRepository() {
		ReqAccountRepository reqAccountRepository = getMapper(ReqAccountRepository.class);
		this.reqAccountRepository = reqAccountRepository;
		this.mapper = reqAccountRepository;
	}

	public ResultStatus<ReqAccount> register(String name, String email, String pass) {

		ReqAccount a = new ReqAccount();
		a.setName(name);
		a.setEmail(email);
		a.setAkey(AESUtil.generateKey());
		String password = null;
		try {
			password = Md5Utils.getT4MD5(pass);
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		a.setPassword(password);

		ResultStatus<ReqAccount> res = new ResultStatus<ReqAccount>();

		reqAccountRepository.insert(a);
		res.setData(a);


		return res;
	}

	public ResultStatus<Pair<String, ReqAccount>> login(String email, String pass) {
		ResultStatus<Pair<String, ReqAccount>> res = new ResultStatus<>();
		if(email == null || pass == null) {
			res.setCode(1005+"");
			return res;
		}
		

		String passwordMD5 = null;
		try {
			passwordMD5 = Md5Utils.getT4MD5(pass);
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);

		}

		Example example = new Example(ReqAccount.class);
		example.createCriteria().andEqualTo("email", email).andEqualTo("password", passwordMD5);
		List<ReqAccount> list = reqAccountRepository.selectByExample(example);

		if (CollectionUtils.isEmpty(list)) {
			res.setCode(1002+"");
			return res;
		}

		ReqAccount a = list.get(0);
		if (a == null) {
			res.setCode(1002+"");
			return res;
		}
		

		
		String loginCookieVal = null;
		try {
			loginCookieVal = new LoginCookieEncrypt(a).genetrateLoginCookieEncryptVal();
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		
		res.setData(Pair.of(loginCookieVal, a) );

		return res;
	}



	public ResultStatus<ReqAccount> sendUpdatePassUrlToEmail(String email) {
		ResultStatus<ReqAccount> res = new ResultStatus<ReqAccount>();

		ReqAccount a = reqAccountRepository.findByEmail(email);
		if (a == null) {
			res.setCode(1003+"");
			return res;
		}

		a.setToken(UUID.randomUUID().toString());

		// TODO get host & create url
		// TODO send email

		return res;
	}

	public ResultStatus<ReqAccount> updatePass(String token, String email, String pass) {
		ResultStatus<ReqAccount> res = new ResultStatus<ReqAccount>();

		String password = null;
		try {
			password = Md5Utils.getT4MD5(pass);
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		ReqAccount a = reqAccountRepository.findByEmailAndToken(email, token);
		if (a == null) {
			res.setCode(1004+"");
			return res;
		}

		a.setPassword(password);

		return res;
	}

}
