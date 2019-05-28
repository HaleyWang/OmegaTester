package com.haleywang.monitor.common.req.converter;

import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.utils.JavaExecScript;
import com.haleywang.monitor.utils.JsonUtils;

@MyRequestExportAnnotation(name = "Custom Script")
@MyRequestImportAnnotation(name = "Custom Script")
public class CustomScriptConverter implements ReqConverter {

    public String fromMyRequest(MyRequest myRequest) {


        return null;
    }



    @Override
    public MyRequest toMyRequest(TypeValuePair o) {
        String reqString = o.getValue();
        String code = o.getCode();
        String myReqString = JavaExecScript.jsRunScriptCode(code, reqString);

        return JsonUtils.fromJson(myReqString, MyRequest.class);
    }

}
