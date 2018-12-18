package com.haleywang.monitor.service;

import com.haleywang.monitor.model.ReqAccount;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.util.List;



public interface BaseService<T> {

	T save(T model);
	T update(T model);

	List<T> findAll(ReqAccount reqAccount);

	//Example example
	List<T> findAll(Example example, RowBounds rowBounds);


	T findOne(T model);

	T findOne(Long id);


	void delete(T model);

	void deleteByPrimaryKey(Object id) ;

}
