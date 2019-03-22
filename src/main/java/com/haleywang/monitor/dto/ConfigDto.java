package com.haleywang.monitor.dto;

import java.util.List;

import com.haleywang.monitor.common.req.HttpMethod;
import lombok.Data;

@Data
public class ConfigDto {

    private HttpMethod[] methods;

    private List<IdValuePair> reqExamples;
    private List<IdValuePair> caseExamples;
    private List<IdValuePair> preScriptExamples;
    private List<IdValuePair> testScriptExamples;


}
