package com.haleywang.monitor.dao;

import java.util.List;

import com.haleywang.db.mapper.MyMapper;

import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReqRelationRepository extends MyMapper<ReqRelation> {

}
