package com.haleywang.monitor.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ReqSetting implements Serializable {

	public static enum SettingType {
		ENV
	}
	

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator= "JDBC")
    private Long id;
    
    private String name;
    
    private Long onwer;

	private Integer current;
    
    private String onwerType;

    private String content;

	@ColumnType(
			column = "type",
			jdbcType = JdbcType.VARCHAR,
			typeHandler = SettingTypeEnumTypeHandler.class)
    private SettingType type;

	public Long getId() {
		return id;
	}


	public String getOnwerType() {
		return onwerType;
	}


	public void setOnwerType(String onwerType) {
		this.onwerType = onwerType;
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


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public SettingType getType() {
		return type;
	}


	public void setType(SettingType type) {
		this.type = type;
	}


	public Long getOnwer() {
		return onwer;
	}


	public void setOnwer(Long onwer) {
		this.onwer = onwer;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}
}
