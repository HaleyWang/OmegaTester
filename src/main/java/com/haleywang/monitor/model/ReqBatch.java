package com.haleywang.monitor.model;

import com.haleywang.monitor.utils.DateUtils;
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

@Entity
public class ReqBatch implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator= "JDBC")
	private Long batchId;
	
	private Long scheduleId;


	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "groupId", nullable = true)
	private ReqGroup reqGroup;
	
	
	private Long envSettingId;

	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "createdById", nullable = true)
	private ReqAccount createdBy;

	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "modifiedById", nullable = true)
	private ReqAccount modifiedBy;

	private String name;

	@ColumnType(column = "enable", jdbcType = JdbcType.BOOLEAN)
	private Boolean enable;

	private String timeExpression;

	private String statuts;

	private Date createdOn;

	private Date updatedOn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBatchId() {
		return batchId;
	}

	public Long getEnvSettingId() {
		return envSettingId;
	}

	public void setEnvSettingId(Long envSettingId) {
		this.envSettingId = envSettingId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public ReqGroup getReqGroup() {
		return reqGroup;
	}

	public void setReqGroup(ReqGroup reqGroup) {
		this.reqGroup = reqGroup;
	}

	public ReqAccount getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ReqAccount createdBy) {
		this.createdBy = createdBy;
	}

	public ReqAccount getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(ReqAccount modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getTimeExpression() {
		return timeExpression;
	}

	public void setTimeExpression(String timeExpression) {
		this.timeExpression = timeExpression;
	}

	public String getStatuts() {
		return statuts;
	}

	public void setStatuts(String statuts) {
		this.statuts = statuts;
	}

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

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

}
