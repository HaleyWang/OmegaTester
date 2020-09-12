package com.haleywang.monitor.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto {

    String code;
    String msg;


}
