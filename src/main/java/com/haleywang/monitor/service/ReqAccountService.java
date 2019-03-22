package com.haleywang.monitor.service;

import org.apache.commons.lang3.tuple.Pair;

import com.haleywang.monitor.dto.ChangePasswordDto;
import com.haleywang.monitor.dto.NewAccountDto;
import com.haleywang.monitor.dto.ResetPasswordDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;

public interface ReqAccountService extends BaseService<ReqAccount> {


	public ResultStatus<ReqAccount> register(String name, String email, String pass) ;

	public ResultStatus<Pair<String, ReqAccount>> login(String email, String pass) ;

	public ResultStatus<ReqAccount> sendUpdatePassUrlToEmail(String email) ;

	public ResultStatus<ReqAccount> updatePass(String token, String email, String pass) ;

	ResultStatus<ReqAccount> register(NewAccountDto dto);

	ResultStatus changePassword(ChangePasswordDto dto);

	ResultStatus resetPassword(ResetPasswordDto dto);

	ResultStatus resetSuperAdminPassword(ResetPasswordDto dto);
}
