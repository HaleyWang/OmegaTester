package com.haleywang.monitor.service;

import com.haleywang.monitor.entity.ReqSetting;

import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqSettingService extends BaseService<ReqSetting> {


    /**
     * findByTypeAndOnwerAndCurrent
     *
     * @param type
     * @param onwer
     * @param current
     * @return
     */
    public ReqSetting findByTypeAndOnwerAndCurrent(ReqSetting.SettingType type, Long onwer, int current);

    /**
     * findByOnwer
     *
     * @param onwer
     * @return
     */
    public List<ReqSetting> findByOnwer(Long onwer);


}
