package com.haleywang.monitor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ReqTaskHistoryMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum HisType {
    	BATCH, MANUAL
    }
    
    @Id
	@GeneratedValue(generator= "JDBC")
    private Long id;
    private Long historyId;
    
	@Transient
    private ReqTaskHistory reqTaskHistory;
	

	@Lob
    private String content;
    
	@Column(length=4000)
    private String testReport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReqTaskHistory getReqTaskHistory() {
		return reqTaskHistory;
	}

	public void setReqTaskHistory(ReqTaskHistory reqTaskHistory) {

		if(reqTaskHistory != null) {
			this.historyId = reqTaskHistory.getHistoryId();
		}
		this.reqTaskHistory = reqTaskHistory;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTestReport() {
		return testReport;
	}

	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}


	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}
}
