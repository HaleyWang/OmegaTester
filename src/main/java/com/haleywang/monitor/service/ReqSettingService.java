package com.haleywang.monitor.service;

import com.haleywang.monitor.entity.ReqSetting;

import java.util.List;

public interface ReqSettingService extends BaseService<ReqSetting> {




    public ReqSetting findByTypeAndOnwerAndCurrent(ReqSetting.SettingType type, Long onwer, int current);

    public List<ReqSetting> findByOnwer(Long onwer);



}
