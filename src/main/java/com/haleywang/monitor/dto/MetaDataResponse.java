package com.haleywang.monitor.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetaDataResponse {
    private ResponseMeta meta;
    private Object data;


}
