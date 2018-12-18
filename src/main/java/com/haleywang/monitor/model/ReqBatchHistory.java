package com.haleywang.monitor.model;

import com.haleywang.monitor.utils.DateUtils;
import lombok.Data;

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



	public Date getBatchStartDate() {
		return DateUtils.copy(batchStartDate);
	}

	public void setBatchStartDate(Date batchStartDate) {
		this.batchStartDate = DateUtils.copy(batchStartDate);
	}




}
