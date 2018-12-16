package com.haleywang.monitor.model;

import com.haleywang.monitor.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ReqBatchHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static enum Statuts {
    	ERROR,  READY,  ACTIVATING,  ACTIVATED,  PROCESSING,  COMPLETED,  CANCELLED
    }

    @Id
	@GeneratedValue(generator= "JDBC")
    private Long batchHistoryId;
    
    private Long batchId;
    
    private String name;

    private Date batchStartDate;
    
    
    private Statuts statuts;
    
    private int successNum;
    private int total;
    
    private long costTime;

	public String getName() {
		return name;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getTotal() {
		return total;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Long getBatchHistoryId() {
		return batchHistoryId;
	}

	public void setBatchHistoryId(Long batchHistoryId) {
		this.batchHistoryId = batchHistoryId;
	}


	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Date getBatchStartDate() {
		return DateUtils.copy(batchStartDate);
	}

	public void setBatchStartDate(Date batchStartDate) {
		this.batchStartDate = DateUtils.copy(batchStartDate);
	}

	public Statuts getStatuts() {
		return statuts;
	}

	public void setStatuts(Statuts statuts) {
		this.statuts = statuts;
	}


}
