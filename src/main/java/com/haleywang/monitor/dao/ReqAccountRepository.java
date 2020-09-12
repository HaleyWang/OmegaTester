package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqAccount;
/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqAccountRepository extends MyMapper<ReqAccount> {

    /**
     * Retrieve a list of mapped object.
     *
     * @param name
     * @return
     */
    public ReqAccount findByName(String name);

    /**
     * Retrieve a list of mapped object.
     *
     * @param email
     * @return
     */
    public ReqAccount findByEmail(String email);

    /**
     * Retrieve a list of mapped object.
     *
     * @param email
     * @param password
     * @return
     */
    public ReqAccount findByEmailAndPassword(String email, String password);

    /**
     * Retrieve a list of mapped object.
     *
     * @param email
     * @param token
     * @return
     */
    public ReqAccount findByEmailAndToken(String email, String token);


}
