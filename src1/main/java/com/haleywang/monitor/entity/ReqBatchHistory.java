package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class ReqBatchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long batchHistoryId;

    private Long batchId;

    private String name;

    private Date batchStartDate;

    @Builder.Default
    @Column(name = "status", length = 30, nullable = true)
    private Status status = Status.READY;

    @Builder.Default
    private Integer successNum = 0;
    @Builder.Default
    private Integer total = 0;

    @Builder.Default
    private Long costTime = 0L;

}
