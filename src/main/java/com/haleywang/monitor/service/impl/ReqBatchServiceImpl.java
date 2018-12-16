package com.haleywang.monitor.service.impl;

import javax.annotation.Resource;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqBatch;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqGroupService;
import org.apache.ibatis.session.SqlSession;

public class ReqBatchServiceImpl extends BaseServiceImpl<ReqBatch> implements
		ReqBatchService {


	//private ReqBatchRepository reqBatchRepository;


	@Resource
	private ReqGroupService reqGroupService;

	@Resource
	private ReqAccountRepository reqAccountRepository;

	public ReqBatchServiceImpl() {
		initRepository();
	}

	@Resource
	public void initRepository() {
		ReqBatchRepository reqBatchRepository = getMapper(ReqBatchRepository.class);
		//this.reqBatchRepository = reqBatchRepository;
		this.reqGroupService = new ReqGroupServiceImpl();
		this.reqAccountRepository = getMapper(ReqAccountRepository.class);
		this.mapper = (reqBatchRepository);
	}

	@Override
	public ReqBatch save(ReqBatch model) {
		ReqAccount createdBy = model.getCreatedBy();
		createdBy = reqAccountRepository.selectOne(createdBy);
		model.setCreatedBy(createdBy);

		model = super.save(model);
		try {
			//TODO haley
			//jobManagerService.addJob(createBatchScheduleJob(model));
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		
		return model;
	}



}
