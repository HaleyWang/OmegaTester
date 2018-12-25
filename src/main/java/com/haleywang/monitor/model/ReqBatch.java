package com.haleywang.monitor.model;

import com.haleywang.monitor.utils.DateUtils;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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




	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Date getCreatedOn() {
		return DateUtils.copy(createdOn);
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = DateUtils.copy(createdOn);
	}

	public Date getUpdatedOn() {
		return DateUtils.copy(updatedOn);
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = DateUtils.copy(updatedOn);
	}



}
