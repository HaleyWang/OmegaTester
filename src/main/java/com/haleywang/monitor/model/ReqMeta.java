package com.haleywang.monitor.model;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.StringTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;

@Entity
public class ReqMeta {
	
	public static enum DataType {
		TAB, SUB_TAB, REQUEST, TEST_SCRIPT, PRE_REQUEST_SCRIPT, DESCRIPTION, CASES
	}


	@Id
	@GeneratedValue(generator= "JDBC")
	private Long metaId;

	private Long reqId;
	

	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "reqId", nullable = true)
    private ReqInfo req;

	//DataType
	@ColumnType(
			column = "data_type",
			jdbcType = JdbcType.VARCHAR,
			typeHandler = DataTypeEnumTypeHandler.class)
	private DataType dataType;

	public Long getReqId() {
		return reqId;
	}

	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}

	@Lob
	@ColumnType(jdbcType = JdbcType.CLOB)
	private String data;

	public Long getMetaId() {
		return metaId;
	}

	public void setMetaId(Long metaId) {
		this.metaId = metaId;
	}

	

	public ReqInfo getReq() {
		return req;
	}

	public void setReq(ReqInfo req) {
		if(req != null) {
			this.reqId = req.getId();
		}

		this.req = req;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
}
