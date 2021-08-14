package com.haleywang.monitor.dto;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.req.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haley
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfigDto {

    private HttpMethod[] methods;

    @Builder.Default
    private List<IdValuePair> reqExamples = new ArrayList<>();
    @Builder.Default
    private List<IdValuePair> caseExamples = new ArrayList<>();
    @Builder.Default
    private List<IdValuePair> preScriptExamples = new ArrayList<>();
    @Builder.Default
    private List<IdValuePair> testScriptExamples = new ArrayList<>();


    public ImmutableMap<String, List<IdValuePair>> examplesMap() {
        return ImmutableMap.of(
                "req", reqExamples,
                "case", caseExamples,
                "pre_script", preScriptExamples,
                "test_script", testScriptExamples);


    }

}
