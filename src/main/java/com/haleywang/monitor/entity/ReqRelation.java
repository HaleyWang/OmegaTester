package com.haleywang.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ReqRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	public ReqRelation(Long objectId, String type, String permission,
					   ReqAccount reqAccount) {
		this.objectId = objectId;
		this.type = type;
		this.permission = permission;
		this.reqAccount = reqAccount;
	}


	@Id
	@GeneratedValue(generator = "JDBC")
	private Long relationId;

	private Long objectId;

	private String type;

	private String permission;

	private Long accountId;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "accountId", nullable = true)
	private ReqAccount reqAccount;


}
