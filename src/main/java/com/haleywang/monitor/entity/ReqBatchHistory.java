package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
