package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqTaskHistory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
public interface ReqTaskHistoryRepository extends MyMapper<ReqTaskHistory> {


	/**
	 * Find ReqTaskHistory list
	 *
	 * @param reqId
	 * @return
	 */
	@Select("select * from req_task_history where req_id =#{reqId} order by history_id desc")
	public List<ReqTaskHistory> findByReq(@Param("reqId") Long reqId);

	/**
	 * * Execute a delete statement.
	 *
	 * @param reqId
	 */
	@Delete("delete from req_task_history where req_id = #{reqId} ")
	public void deleteByReqId(Long reqId);


}
