package com.haleywang.monitor.entity;

import org.apache.ibatis.type.EnumTypeHandler;

/**
 * Created by haley on 2018/8/19.
 */
public class SettingTypeEnumTypeHandler extends EnumTypeHandler<ReqSetting.SettingType> {

        public SettingTypeEnumTypeHandler(Class<ReqSetting.SettingType> type) {
                super(type);
        }


}


