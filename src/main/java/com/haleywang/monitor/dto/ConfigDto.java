package com.haleywang.monitor.dto;

import com.mashape.unirest.http.HttpMethod;
import lombok.Data;

import java.util.List;

@Data
public class ConfigDto {

    private HttpMethod[] methods;

    private List<IdValuePair> reqExamples;
    private List<IdValuePair> caseExamples;
    private List<IdValuePair> preScriptExamples;
    private List<IdValuePair> testScriptExamples;


}
