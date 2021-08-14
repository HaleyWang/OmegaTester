package com.haleywang.monitor.entity;

import org.apache.ibatis.type.EnumTypeHandler;

/**
 * @author haley
 * @date 2018/12/16
 */
public class DataTypeEnumTypeHandler extends EnumTypeHandler<ReqMeta.DataType> {

public DataTypeEnumTypeHandler(Class<ReqMeta.DataType> type) {
        super(type);
        }
        }


