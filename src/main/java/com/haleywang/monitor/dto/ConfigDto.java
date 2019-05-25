package com.haleywang.monitor.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.req.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigDto {

    private HttpMethod[] methods;


    private List<IdValuePair> reqExamples = new ArrayList<>();
    private List<IdValuePair> caseExamples = new ArrayList<>();
    private List<IdValuePair> preScriptExamples = new ArrayList<>();
    private List<IdValuePair> testScriptExamples = new ArrayList<>();


    public ImmutableMap<String, List<IdValuePair>> examplesMap() {
        return ImmutableMap.of(
                "req", reqExamples,
                "case", caseExamples,
                "pre_script", preScriptExamples,
                "test_script", testScriptExamples);


    }

}
