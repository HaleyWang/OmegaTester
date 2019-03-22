package com.haleywang.monitor.service.impl;

import javax.annotation.Resource;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;
import com.haleywang.monitor.schedule.CronScheduleHelper;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.utils.AESUtil;
import org.apache.ibatis.session.SqlSession;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;

public class ReqBatchServiceImpl extends BaseServiceImpl<ReqBatch> implements
		ReqBatchService {

	@Resource
	private ReqGroupService reqGroupService;

	@Resource
	private ReqAccountRepository reqAccountRepository;

	public ReqBatchServiceImpl() {
		initRepository();
	}

	private void initRepository() {
		ReqBatchRepository reqBatchRepository = getMapper(ReqBatchRepository.class);
		//this.reqBatchRepository = reqBatchRepository;
		this.reqGroupService = new ReqGroupServiceImpl();
		this.reqAccountRepository = getMapper(ReqAccountRepository.class);
		this.mapper = (reqBatchRepository);
	}

	@Override
	public ReqBatch save(ReqBatch model) {
		Long createdBy = model.getCreatedById();
		if(model.getVersion() == null) {
			model.setVersion(0L);
		}

		model = super.save(model);
		try {
			//TODO haley
			//jobManagerService.addJob(createBatchScheduleJob(model));
		} catch (Exception e) {
			throw new ReqException(e.getMessage(), e);
		}
		
		return model;
	}

	public int updateByVersion(ReqBatch model) {
		Long createdBy = model.getCreatedById();
		Long version = model.getVersion();

		Example example = new Example(ReqBatch.class);
		example.createCriteria().andEqualTo("version", version).andEqualTo("batchId", model.getBatchId());
		model.setVersion(version+1);
		return this.mapper.updateByExample(model, example);
	}

	@Override
	public ReqBatch save(ReqBatch model, ReqAccount reqAccount) {
		model.setCreatedById(reqAccount.getAccountId());
		return save(model);
	}

	@Override
	public void initDb() {

		SqlSession session = DBUtils.getOrOpenSqlSession();
		try {
			//outputInitSql(session);
			//demoBlog(session);
			try {

				DBUtils.doInitSql(session);
			} catch (IOException e) {
				e.printStackTrace();
			}

			ReqAccountRepository mapper = session.getMapper(ReqAccountRepository.class);

			ReqAccount rb = new ReqAccount();
			mapper.deleteByPrimaryKey(1L);
			rb.setName("a@a.com");
			rb.setEmail("a@a.com");
			rb.setPassword("f4cc399f0effd13c888e310ea2cf5399");
			rb.setAkey(AESUtil.generateKey());
			mapper.insert(rb);
			List<ReqAccount> list = mapper.selectAll();
			System.out.println(list.size());
			System.out.println(list.get(0));

			DBUtils.commitSession(session);

		} finally {
			DBUtils.closeSession(session);
		}

	}

	@Override
	public void update(ReqBatch reqBatch, ReqAccount reqAccount) {
		super.update(reqBatch);
		CronScheduleHelper.putSchedule(reqBatch);
	}
}
