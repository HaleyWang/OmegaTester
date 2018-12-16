package com.haleywang.monitor.service;

import com.haleywang.db.mapper.Sort;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.io.Closeable;
import java.util.List;



public interface BaseService<T> {

	T save(T model);
	T update(T model);


	List<T> findAll();

	//Example example
	List<T> findAll(Example example, RowBounds rowBounds);


	T findOne(T model);

	T findOne(Long id);


	void delete(T model);

	void deleteByPrimaryKey(Object id) ;

}
