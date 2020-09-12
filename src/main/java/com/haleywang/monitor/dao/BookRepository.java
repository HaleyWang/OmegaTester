package com.haleywang.monitor.dao;


import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.entity.Book;

/**
 * @author haley
 */
public interface BookRepository extends MyMapper<Book> {

    /**
     * etrieve a single row mapped
     *
     * @param name
     * @return
     */
    public Book findByName(String name);

}
