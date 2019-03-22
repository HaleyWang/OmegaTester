package com.haleywang.monitor.dao;

import java.util.List;

import com.haleywang.db.mapper.MyMapper;

import com.haleywang.monitor.entity.ReqTaskHistory;

import org.apache.ibatis.annotations.*;

public interface ReqTaskHistoryRepository extends MyMapper<ReqTaskHistory> {


	@Select("select * from req_task_history where req_id =#{reqId} order by history_id desc")
	public List<ReqTaskHistory> findByReq(@Param("reqId") Long reqId);

	//Query("delete from ReqTaskHistory where req.id = :reqId")
	@Delete("delete from req_task_history where req_id = #{reqId} ")
	public void deleteByReqId(Long reqId);


}
