package com.haleywang.monitor.entity;

import org.apache.ibatis.type.EnumTypeHandler;


/**
 * @author haley
 */
public class SettingTypeEnumTypeHandler extends EnumTypeHandler<ReqSetting.SettingType> {

        public SettingTypeEnumTypeHandler(Class<ReqSetting.SettingType> type) {
                super(type);
        }


}


