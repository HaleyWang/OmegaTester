package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.io.Serializable;


/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqTaskHistoryMeta implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum HisType {
		/**
		 * Options of enum
		 */
		BATCH, MANUAL
	}

	@Id
	@GeneratedValue(generator = "JDBC")
	private Long id;
	private Long taskHistoryId;

	@Transient
	private ReqTaskHistory reqTaskHistory;


	@Lob
	@ColumnType(jdbcType = JdbcType.CLOB)
	private String content;

	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Column(length = 4000)
	private String testReport;


	public void setReqTaskHistory(ReqTaskHistory reqTaskHistory) {

		if(reqTaskHistory != null) {
			this.taskHistoryId = reqTaskHistory.getTaskHistoryId();
		}
		this.reqTaskHistory = reqTaskHistory;
	}


}
