package com.haleywang.monitor.dao;

import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.model.ReqMeta;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReqMetaRepository extends MyMapper<ReqMeta> {


}
