package com.haleywang.monitor.common.req.converter;

import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.dto.TypeValuePair;
import com.haleywang.monitor.utils.JavaExecScript;
import com.haleywang.monitor.utils.JsonUtils;
/**
 * @author haley
 * @date 2018/12/16
 */
@MyRequestExportAnnotation(name = "Custom Script")
@MyRequestImportAnnotation(name = "Custom Script")
public class CustomScriptConverter implements ReqConverter {

    @Override
    public String fromMyRequest(TypeValuePair o) {
        return JavaExecScript.jsRunScriptCode(o.getCode(), o.getValue());
    }



    @Override
    public MyRequest toMyRequest(TypeValuePair o) {
        String reqString = o.getValue();
        String code = o.getCode();
        String myReqString = JavaExecScript.jsRunScriptCode(code, reqString);

        return JsonUtils.fromJson(myReqString, MyRequest.class);
    }

}
