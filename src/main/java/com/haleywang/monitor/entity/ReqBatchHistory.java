package com.haleywang.monitor.entity;

import com.haleywang.monitor.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
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
    
    private Integer successNum = 0;
    private Integer total = 0;
    
    private Long costTime = 0L;



	public Date getBatchStartDate() {
		return DateUtils.copy(batchStartDate);
	}

	public void setBatchStartDate(Date batchStartDate) {
		this.batchStartDate = DateUtils.copy(batchStartDate);
	}




}