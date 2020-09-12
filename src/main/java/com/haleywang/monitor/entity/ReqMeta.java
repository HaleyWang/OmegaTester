package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqMeta {

	public static enum DataType {
		/**
		 * Options of enum
		 */
		TAB, SUB_TAB, REQUEST, TEST_SCRIPT, PRE_REQUEST_SCRIPT, DESCRIPTION, CASES
	}


	@Id
	@GeneratedValue(generator = "JDBC")
	private Long metaId;

	private Long reqId;


	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "reqId", nullable = true)
    private ReqInfo req;

	@ColumnType(
			column = "data_type",
			jdbcType = JdbcType.VARCHAR,
			typeHandler = DataTypeEnumTypeHandler.class)
	private DataType dataType;

	@Lob
	@ColumnType(jdbcType = JdbcType.CLOB)
	private String data;

	public void setReq(ReqInfo req) {
		if(req != null) {
			this.reqId = req.getId();
		}

		this.req = req;
	}


}
