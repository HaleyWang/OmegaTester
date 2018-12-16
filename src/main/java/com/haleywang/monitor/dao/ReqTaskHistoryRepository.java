package com.haleywang.monitor.dao;

import java.util.List;

import com.haleywang.db.mapper.MyMapper;


import com.haleywang.db.mapper.Sort;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.model.ReqTaskHistory;
import com.haleywang.monitor.model.ReqTaskHistory.HisType;
import org.apache.ibatis.annotations.*;

public interface ReqTaskHistoryRepository extends MyMapper<ReqTaskHistory> {


	@Select("select * from req_task_history where req_id =#{reqId} order by history_id desc")
	public List<ReqTaskHistory> findByReq(@Param("reqId") Long reqId);

	//Query("delete from ReqTaskHistory where req.id = :reqId")
	@Delete("delete from req_task_history where req_id = #{reqId} ")
	public void deleteByReqId(Long reqId);


	@Select("select history_id as historyId," +
			"batch_history_id as batch_historyId," +
			"created_by_id as createdById," +
			"created_on as createdOn," +
			"test_statuts as testStatuts," +
			"statuts," +
			"statut_code as statutCode," +
			"test_success as testSuccess," +
			"his_type as hisType," +
			"req_id as reqId," +
			"account_id as accountId" +
			"        from req_task_history where created_by_id =#{accountId} and req_id =#{reqId} and his_type=#{hisType} order by history_id desc")

	public List<ReqTaskHistory> findByCreatedByIdAndHisType(
	@Param("accountId") Long accountId,
	@Param("reqId") Long reqId,
	@Param("hisType") HisType hisType);

	@Select("select history_id as historyId," +
			"batch_history_id as batch_historyId," +
			"created_by_id as createdById," +
			"created_on as createdOn," +
			"test_statuts as testStatuts," +
			"statuts," +
			"statut_code as statutCode," +
			"test_success as testSuccess," +
			"his_type as hisType," +
			"req_id as reqId," +
			"account_id as accountId" +
			" from req_task_history where batchHistoryId =#{batchHistoryId} order by history_id desc")
	public List<ReqTaskHistory> findByBatchHistoryId(@Param("batchHistoryId") Long batchHistoryId);

}
