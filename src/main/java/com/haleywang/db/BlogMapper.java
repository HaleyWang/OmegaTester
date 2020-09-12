package com.haleywang.db;

import com.haleywang.db.mapper.MyMapper;

/**
 * @author haley
 * @date 2018/12/16
 */
public interface BlogMapper extends MyMapper<Blog> {


    /**
     * etrieve a single row mapped
     *
     * @param id
     * @return
     */
    Blog selectBlog(long id);
}
