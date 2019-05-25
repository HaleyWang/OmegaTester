package com.haleywang.monitor.entity;

import com.haleywang.monitor.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqTaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum HisType {
    	BATCH, MANUAL
    }
    
    @Id
	@GeneratedValue(generator= "JDBC")
    private Long taskHistoryId;

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

    public boolean isSuccess() {
        return (!"false".equals(testSuccess));
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = DateUtils.copy(createdOn);
    }


}
