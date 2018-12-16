package com.haleywang.db;

import com.haleywang.db.mapper.MyMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by haley on 2018/8/15.
 */
public interface BlogMapper extends MyMapper<Blog> {


    Blog selectBlog(long id);
}
