package com.haleywang.monitor.service;

import com.haleywang.monitor.entity.ReqAccount;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author haley
 */
public interface BaseService<T> {

	/**
	 * Execute an insert statement.
	 *
	 * @param model
	 * @return
	 */
	T save(T model);

	/**
	 * Execute an update statement.
	 *
	 * @param model
	 * @return
	 */
	T update(T model);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param reqAccount
	 * @return
	 */
	List<T> findAll(ReqAccount reqAccount);


	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param example
	 * @param rowBounds
	 * @return
	 */
	List<T> findAll(Example example, RowBounds rowBounds);

	/**
	 * Retrieve a list of mapped objects.
	 *
	 * @param example
	 * @return
	 */
	List<T> findAll(Example example);

	/**
	 * Retrieve a single row mapped from model
	 *
	 * @param model
	 * @return
	 */
	T findOne(T model);

	/**
	 * Retrieve a single row mapped from id
	 *
	 * @param id
	 * @return
	 */
	T findOne(Long id);


	/**
	 * Execute a delete statement.
	 *
	 * @param model
	 */
	void delete(T model);

	/**
	 * Execute a delete statement.
	 *
	 * @param id
	 */
	void deleteByPrimaryKey(Object id);

	/**
	 * Execute an save or update statement.
	 *
	 * @param model
	 * @return
	 */
	T saveOrUpdate(T model);

}
