package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReqBatch implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		/**
		 * Options of enum
		 */
		RUNNING
	}

	@Id
	@GeneratedValue(generator = "JDBC")
	private Long batchId;

	private Long scheduleId;

	private Long envSettingId;

	private Long groupId;
	private Long version;

	private Long createdById;
	private Long modifiedById;

	private String name;

	@ColumnType(column = "enable", jdbcType = JdbcType.BOOLEAN)
	private Boolean enable;

	private String timeExpression;

	private String status;

	private Date createdOn;

	private Date updatedOn;


}
