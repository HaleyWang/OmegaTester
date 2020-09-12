package com.haleywang.monitor.service;

import com.haleywang.monitor.common.LoginMsg;
import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.LoginResultDto;
import com.haleywang.monitor.dto.Message;
import com.haleywang.monitor.dto.NewAccountDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;

import java.lang.reflect.InvocationTargetException;

/**
 * @author haley
 */
public interface ReqAccountService extends BaseService<ReqAccount> {


	/**
	 * register
	 *
	 * @param name
	 * @param email
	 * @param pass
	 * @return
	 */
	public ResultStatus<ReqAccount> register(String name, String email, String pass);

	/**
	 * login
	 *
	 * @param email
	 * @param pass
	 * @return
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public ResultMessage<LoginResultDto, LoginMsg> login(String email, String pass)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

	/**
	 * sendUpdatePassUrlToEmail
	 *
	 * @param email
	 * @return
	 */
	public ResultMessage<ReqAccount, Message> sendUpdatePassUrlToEmail(String email);

	/**
	 * updatePass
	 *
	 * @param token
	 * @param email
	 * @param pass
	 * @return
	 */
	public ResultStatus<ReqAccount> updatePass(String token, String email, String pass);

	/**
	 * register
	 *
	 * @param dto
	 * @return
	 */
	ResultStatus<ReqAccount> register(NewAccountDto dto);

	/**
	 * changePassword
	 *
	 * @param dto
	 * @return
	 */
	ResultStatus changePassword(ChangePasswordDto dto);

	/**
	 * resetPassword
	 *
	 * @param dto
	 * @return
	 */
	ResultStatus resetPassword(ResetPasswordDto dto);

	/**
	 * resetSuperAdminPassword
	 *
	 * @param dto
	 * @return
	 */
	ResultStatus resetSuperAdminPassword(ResetPasswordDto dto);
}
