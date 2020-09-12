package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;


/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqSetting implements Serializable {

	public static enum SettingType {
		/**
		 * Options of enum
		 */
		ENV, CODE_FOR_IMPORT, CODE_FOR_EXPORT
	}


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "JDBC")
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
