package com.haleywang.monitor.service.impl;

import com.haleywang.db.DBUtils;
import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.service.BaseService;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public class BaseServiceImpl<T> implements BaseService<T> {
	
	protected MyMapper<T> mapper;
	private SqlSession session;


	public BaseServiceImpl() {
		this.session = DBUtils.getOrOpenSqlSession();
	}

	protected final void rollback() {
		session.rollback();
	}

	protected <T> T getMapper(Class<T> type) {
		return session.getMapper(type);
	}

	@Override
	public T save(T model) {
		mapper.insert(model);
		return model;
	}

	public T saveOrUpdate(T model) {
		if(findOne(model) != null) {
			update(model);
		}else {
			save(model);
		}
		return model;
	}

	public T update(T model) {
		mapper.updateByPrimaryKey(model);
		return model;
	}

	@Override
	public List<T> findAll(ReqAccount reqAccount) {
		return mapper.selectAll();
	}

	@Override
	public List<T> findAll(Example example, RowBounds rowBounds) {
		return mapper.selectByExampleAndRowBounds(example, rowBounds);
	}

	@Override
	public List<T> findAll(Example example) {
		return mapper.selectByExample(example);
	}

	@Override
	public void delete(T t) {
		mapper.delete(t);
	}

	@Override
	public void deleteByPrimaryKey(Object id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public T findOne(T t) {
		return mapper.selectOne(t);
	}

	@Override
	public T findOne(Long id) {
		return mapper.selectByPrimaryKey(id);
	}


}
