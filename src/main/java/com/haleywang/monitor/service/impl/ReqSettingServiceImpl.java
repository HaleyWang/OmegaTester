package com.haleywang.monitor.service.impl;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.dao.ReqSettingRepository;
import com.haleywang.monitor.model.ReqRelation;
import com.haleywang.monitor.model.ReqSetting;
import com.haleywang.monitor.service.ReqSettingService;
import org.apache.ibatis.session.SqlSession;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

public class ReqSettingServiceImpl extends BaseServiceImpl<ReqSetting> implements ReqSettingService {
	
	private ReqSettingRepository reqSettingRepository;

	public ReqSettingServiceImpl() {
		setReqSettingRepository();
	}

	public void setReqSettingRepository() {
		ReqSettingRepository reqSettingRepository = getMapper(ReqSettingRepository.class);
		this.reqSettingRepository = reqSettingRepository;
		this.mapper = (reqSettingRepository);
	}



	@Override
	public ReqSetting findByTypeAndOnwerAndCurrent(ReqSetting.SettingType type, Long onwer, int current) {

        Example reqSettingExample = new Example(ReqSetting.class);
        reqSettingExample.createCriteria().andEqualTo("type", type)
                .andEqualTo("onwer", onwer)
                .andEqualTo("current", current);


        return reqSettingRepository.selectOneByExample(reqSettingExample);

	}



	@Override
	public List<ReqSetting> findByOnwer(Long onwer) {

        Example reqSettingExample = new Example(ReqSetting.class);
        reqSettingExample.createCriteria()
                .andEqualTo("onwer", onwer)
        ;

        return reqSettingRepository.selectByExample( reqSettingExample);
	}
}
