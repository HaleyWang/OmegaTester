package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.ReqInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReqInfoRepository extends MyMapper<ReqInfo> {


    @Results({
            @Result(column="id",property="id"),
            @Result(column="url",property="url"),
            @Result(column="name",property="name")
    })
    @Select("select * from req_info where id in (select req_id from req_task_history WHERE created_by_id = #{createdById} and his_type = #{hisType} order by task_history_id desc limit 0, 1000) ")
    List<ReqInfo> findHistoryReqInfo(@Param("createdById") Long createdById, @Param("hisType") String hisType);

}
