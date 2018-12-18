package com.haleywang.monitor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@Entity
public class ReqTaskHistoryMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum HisType {
    	BATCH, MANUAL
    }
    
    @Id
	@GeneratedValue(generator= "JDBC")
    private Long id;
    private Long taskHistoryId;
    
	@Transient
    private ReqTaskHistory reqTaskHistory;
	

	@Lob
	@ColumnType(jdbcType = JdbcType.CLOB)
	private String content;

	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Column(length=4000)
    private String testReport;


	public void setReqTaskHistory(ReqTaskHistory reqTaskHistory) {

		if(reqTaskHistory != null) {
			this.taskHistoryId = reqTaskHistory.getTaskHistoryId();
		}
		this.reqTaskHistory = reqTaskHistory;
	}


}
