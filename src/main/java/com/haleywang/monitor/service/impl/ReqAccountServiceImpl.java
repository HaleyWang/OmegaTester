package com.haleywang.monitor.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.haleywang.monitor.common.LoginMsg;
import com.haleywang.monitor.common.NotFoundException;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.login.LoginCookieEncrypt;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.LoginResultDto;
import com.haleywang.monitor.dto.Message;
import com.haleywang.monitor.dto.NewAccountDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.service.ReqAccountService;
import com.haleywang.monitor.utils.AESUtil;
import com.haleywang.monitor.utils.CollectionUtils;
import com.haleywang.monitor.utils.Md5Utils;
import com.haleywang.monitor.utils.StringTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class ReqAccountServiceImpl extends BaseServiceImpl<ReqAccount> implements ReqAccountService {

	public static final String ACCOUNT_NOT_FOUND = "Account not found";
	private ReqAccountRepository reqAccountRepository;

	public ReqAccountServiceImpl() {
		setReqAccountRepository();
	}

	private void setReqAccountRepository() {
		this.reqAccountRepository = getMapper(ReqAccountRepository.class);
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

		ResultStatus<ReqAccount> res = new ResultStatus<>();

		reqAccountRepository.insert(a);
		res.setData(a);

		NewAccountDto dto = new NewAccountDto();
		dto.setName(name);
		dto.setEmail(email);
		dto.setPassword(password);
		return register(dto);
	}

	public ResultMessage<LoginResultDto, LoginMsg> login(String email, String pass)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		ResultMessage<LoginResultDto, LoginMsg> res = new ResultMessage<>();
		if(email == null || pass == null) {
			res.ofMessage(LoginMsg.EMAIL_OR_PASSWORD_IS_INCORRECT);
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
			res.ofMessage(LoginMsg.EMAIL_OR_PASSWORD_IS_INCORRECT);
			return res;
		}

		ReqAccount a = list.get(0);
		if (a == null) {
			res.ofMessage(LoginMsg.EMAIL_OR_PASSWORD_IS_INCORRECT);
			return res;
		}

		String loginCookieVal = null;
		try {
			loginCookieVal = new LoginCookieEncrypt(a).genetrateLoginCookieEncryptVal();
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}

		LoginResultDto loginResultDto = new LoginResultDto(loginCookieVal, a);
		res.setData(loginResultDto);
		return res;
	}


	public ResultMessage<ReqAccount, Message> sendUpdatePassUrlToEmail(String email) {
		ResultMessage<ReqAccount, Message> res = new ResultMessage<>();

		ReqAccount a = reqAccountRepository.findByEmail(email);
		if (a == null) {
			res.ofMessage(null);
			return res;
		}

		a.setToken(UUID.randomUUID().toString());

		// TODO get host & create url
		// TODO send email

		return res;
	}

	public ResultStatus<ReqAccount> updatePass(String token, String email, String pass) {
		ResultStatus<ReqAccount> res = new ResultStatus<>();

		String password = null;
		try {
			password = Md5Utils.getT4MD5(pass);
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		ReqAccount a = reqAccountRepository.findByEmailAndToken(email, token);
		if (a == null) {
			//res.ofCode(1004+"");
			return res;
		}

		a.setPassword(password);

		return res;
	}

	@Override
	public ResultStatus<ReqAccount> register(NewAccountDto dto) {
		Preconditions.checkArgument(StringTool.isValidEmail(dto.getEmail()), "Email address is not legal");

		ReqAccount a = new ReqAccount();
		a.setName(dto.getName());
		if(StringUtils.isBlank(a.getName())) {
			a.setName(Splitter.on('@').splitToList(dto.getEmail()).get(0));
		}
		a.setEmail(dto.getEmail());
		a.setAkey(AESUtil.generateKey());
		String password = null;
		try {
			password = Md5Utils.getT4MD5(dto.getPassword());
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		a.setPassword(password);

		ResultStatus<ReqAccount> res = new ResultStatus<>();

		reqAccountRepository.insert(a);
		res.setData(a);

		return res;
	}

	@Override
	public ResultStatus changePassword(ChangePasswordDto dto) {


		ResultStatus<ReqAccount> res = new ResultStatus<>();

		String password = Md5Utils.getT4MD5(dto.getPassword());

		ReqAccount a = reqAccountRepository.findByEmail(dto.getEmail());

		if (a == null) {
			throw new NotFoundException(ACCOUNT_NOT_FOUND);
		}

		if(a.getPassword().equals(Md5Utils.getT4MD5(dto.getOldPassword()))) {
			//res.ofCode("1005");
			return res;
		}

		a.setPassword(password);
		reqAccountRepository.updateByPrimaryKey(a);
		return res;
	}

	@Override
	public ResultStatus resetPassword(ResetPasswordDto dto) {
		ResultStatus<ReqAccount> res = new ResultStatus<>();

		String password = null;
		try {
			password = Md5Utils.getT4MD5(dto.getPassword());
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		ReqAccount a = reqAccountRepository.findByEmail(dto.getEmail());
		if (a == null) {
			throw new NotFoundException(ACCOUNT_NOT_FOUND);
		}

		a.setPassword(password);
		reqAccountRepository.updateByPrimaryKey(a);
		return res;
	}

	@Override
	public ResultStatus resetSuperAdminPassword(ResetPasswordDto dto) {
		return resetPassword(dto);
	}
}
