package com.haleywang.monitor.service;

import com.haleywang.monitor.model.ReqSetting;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReqSettingService extends BaseService<ReqSetting> {




    public ReqSetting findByTypeAndOnwerAndCurrent(ReqSetting.SettingType type, Long onwer, int current);

    public List<ReqSetting> findByOnwer(Long onwer);



}
