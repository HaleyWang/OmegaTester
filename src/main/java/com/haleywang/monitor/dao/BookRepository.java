package com.haleywang.monitor.dao;


import com.haleywang.db.mapper.MyMapper;
import com.haleywang.monitor.model.Book;

public interface BookRepository extends MyMapper<Book> {

    public Book findByName(String name);

}
