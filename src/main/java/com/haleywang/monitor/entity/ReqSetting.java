package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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

	@ColumnType(jdbcType = JdbcType.CLOB)
	private String content;

	@ColumnType(
			column = "type",
			jdbcType = JdbcType.VARCHAR,
			typeHandler = SettingTypeEnumTypeHandler.class)
    private SettingType type;

}
