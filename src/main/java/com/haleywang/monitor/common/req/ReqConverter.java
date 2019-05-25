package com.haleywang.monitor.common.req;

import com.haleywang.monitor.dto.MyRequest;

public interface ReqConverter {

    MyRequest toMyRequest(String o);

    String fromMyRequest(MyRequest myRequest);
}
