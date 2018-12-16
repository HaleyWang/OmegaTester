package com.haleywang.monitor.model;

import com.haleywang.monitor.utils.DateUtils;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
public class ReqTaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum HisType {
    	BATCH, MANUAL
    }
    
    @Id
	@GeneratedValue(generator= "JDBC")
    private Long historyId;

    private Long batchHistoryId;
	private Long reqId;

	@ColumnType(column = "his_type", jdbcType = JdbcType.VARCHAR)
	private HisType hisType;

    private Long createdById;

    private Date createdOn;


    @Transient
    private ReqInfo req;

    @Transient
    ReqTaskHistoryMeta reqTaskHistoryMeta;

    private String testStatuts;

	private String statuts;
	private String statutCode;

    private String testSuccess;

    public Date getCreatedOn() {
        return DateUtils.copy(createdOn);
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = DateUtils.copy(createdOn);
    }


}
