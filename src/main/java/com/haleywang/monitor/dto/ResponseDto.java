package com.haleywang.monitor.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by haley on 2017/7/20.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto {

    String code;
    String msg;


}
