package com.haleywang.monitor.service;

import com.haleywang.monitor.common.LoginMsg;
import com.haleywang.monitor.dto.LoginResultDto;
import com.haleywang.monitor.dto.Message;
import com.haleywang.monitor.dto.ResultMessage;
import org.apache.commons.lang3.tuple.Pair;

import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.NewAccountDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;

import java.lang.reflect.InvocationTargetException;

public interface ReqAccountService extends BaseService<ReqAccount> {


	public ResultStatus<ReqAccount> register(String name, String email, String pass) ;

	public ResultMessage<LoginResultDto, LoginMsg> login(String email, String pass)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

	public ResultMessage<ReqAccount, Message> sendUpdatePassUrlToEmail(String email) ;

	public ResultStatus<ReqAccount> updatePass(String token, String email, String pass) ;

	ResultStatus<ReqAccount> register(NewAccountDto dto);

	ResultStatus changePassword(ChangePasswordDto dto);

	ResultStatus resetPassword(ResetPasswordDto dto);

	ResultStatus resetSuperAdminPassword(ResetPasswordDto dto);
}
