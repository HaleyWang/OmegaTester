package com.haleywang.monitor.common.req.converter;

import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.TypeValuePair;


/**
 * @author haley
 */
public interface ReqConverter {

    /**
     * Conver to my request
     *
     * @param o
     * @return
     */
    MyRequest toMyRequest(TypeValuePair o);

    /**
     * Conver to string
     *
     * @param o
     * @return
     */
    String fromMyRequest(TypeValuePair o);
}
