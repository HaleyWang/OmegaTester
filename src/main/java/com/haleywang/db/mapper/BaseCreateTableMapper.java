package com.haleywang.db.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author haley
 */
@RegisterMapper
public interface BaseCreateTableMapper<T> {

    /**
     * 查询全部结果
     *
     * @return
     */
    @SelectProvider(type = BaseCreateTableProvider.class, method = "dynamicSQL")
    void createTableSql();
}