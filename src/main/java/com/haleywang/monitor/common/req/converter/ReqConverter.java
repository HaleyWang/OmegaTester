package com.haleywang.monitor.common.req.converter;

import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.TypeValuePair;

public interface ReqConverter {

    MyRequest toMyRequest(TypeValuePair o);

    String fromMyRequest(MyRequest myRequest);
}
