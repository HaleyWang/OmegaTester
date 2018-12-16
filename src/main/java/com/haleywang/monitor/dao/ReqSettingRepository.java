package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;

import com.haleywang.monitor.model.ReqSetting;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReqSettingRepository extends MyMapper<ReqSetting> {

}
