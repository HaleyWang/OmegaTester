package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;

import com.haleywang.monitor.entity.ReqAccount;

public interface ReqAccountRepository extends MyMapper<ReqAccount> {

    public ReqAccount findByName(String name);
    
    public ReqAccount findByEmail(String email);
    
    public ReqAccount findByEmailAndPassword(String email, String password);

	public ReqAccount findByEmailAndToken(String email, String token);


}
