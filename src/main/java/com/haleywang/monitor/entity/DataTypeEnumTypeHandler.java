package com.haleywang.monitor.entity;

import org.apache.ibatis.type.EnumTypeHandler;

/**
 * Created by haley on 2018/8/19.
 */
public class DataTypeEnumTypeHandler extends EnumTypeHandler<ReqMeta.DataType> {

public DataTypeEnumTypeHandler(Class<ReqMeta.DataType> type) {
        super(type);
        }
        }


