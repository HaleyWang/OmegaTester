package com.haleywang.monitor.entity;

import com.haleywang.monitor.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * @author haley
 * @date 2018/12/16
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReqTaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum HisType {
        /**
         * Options of enum
         */
        BATCH, MANUAL
    }

    @Id
    @GeneratedValue(generator = "JDBC")
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

    private String testStatus;

	private String status;
	private String statusCode;

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
