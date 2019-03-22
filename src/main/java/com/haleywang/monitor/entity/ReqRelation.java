package com.haleywang.monitor.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ReqRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public ReqRelation() {}
    

    public ReqRelation(Long objectId, String type, String permission,
			ReqAccount reqAccount) {
		super();
		this.objectId = objectId;
		this.type = type;
		this.permission = permission;
		this.reqAccount = reqAccount;
	}


	@Id
	@GeneratedValue(generator= "JDBC")
    private Long relationId;
    
    private Long objectId;

    private String type;
    
    private String permission;

	private Long accountId;
    
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "accountId", nullable = true)
    private ReqAccount reqAccount;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ReqAccount getReqAccount() {
		return reqAccount;
	}

	public void setReqAccount(ReqAccount reqAccount) {
		this.reqAccount = reqAccount;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
