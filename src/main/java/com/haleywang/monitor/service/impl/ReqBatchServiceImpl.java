package com.haleywang.monitor.service.impl;

import com.haleywang.db.DBUtils;
import com.haleywang.monitor.common.ReqSqlException;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqBatchRepository;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqBatch;
import com.haleywang.monitor.schedule.CronScheduleHelper;
import com.haleywang.monitor.service.ReqBatchService;
import com.haleywang.monitor.utils.AESUtil;
import org.apache.ibatis.session.SqlSession;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;

public class ReqBatchServiceImpl extends BaseServiceImpl<ReqBatch> implements
		ReqBatchService {


	public ReqBatchServiceImpl() {
		initRepository();
	}

	private void initRepository() {
		this.mapper = getMapper(ReqBatchRepository.class);
	}

	@Override
	public ReqBatch save(ReqBatch model) {
		if(model.getVersion() == null) {
			model.setVersion(0L);
		}

		model = super.save(model);

		return model;
	}

	public int updateByVersion(ReqBatch model) {
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
			try {

				DBUtils.doInitSql(session);
			} catch (IOException e) {
				throw new ReqSqlException(e);
			}

			ReqAccountRepository mapper = session.getMapper(ReqAccountRepository.class);

			ReqAccount rb = new ReqAccount();
			mapper.deleteByPrimaryKey(1L);
			rb.setName("a@a.com");
			rb.setEmail("a@a.com");
			rb.setPassword("f4cc399f0effd13c888e310ea2cf5399");
			rb.setAkey(AESUtil.generateKey());
			mapper.insert(rb);

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
