package com.haleywang.monitor.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;



@Builder
@AllArgsConstructor
@NoArgsConstructor
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
