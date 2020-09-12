package com.haleywang.db;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author haley
 */
public class Blog {

    @Id
    @GeneratedValue(generator= "JDBC")
    Long id;

    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @Column(length=2048)
    String name;

    public Blog() {
    }
    public Blog(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
