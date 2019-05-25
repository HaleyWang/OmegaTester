package com.haleywang.monitor.entity;

import com.haleywang.monitor.utils.DateUtils;
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
import java.util.Date;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqBatch implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Status {
		RUNNING
	}

	@Id
	@GeneratedValue(generator= "JDBC")
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
