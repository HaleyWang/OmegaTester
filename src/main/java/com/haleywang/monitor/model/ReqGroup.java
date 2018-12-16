package com.haleywang.monitor.model;

import com.haleywang.monitor.common.ReqException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ReqGroup implements Serializable , ReqGroupItem, Cloneable  {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator= "JDBC")
    private Long groupId;
    
    private Long parentId;

	private Long createdById;
	private Long modifiedById;

    private String name;
    
    private Integer sort;
    
    @ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "createdById", nullable = true)
    private ReqAccount createdBy;
    
    @ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "modifiedById", nullable = true)
    private ReqAccount modifiedBy;

    @Transient
	private List<ReqGroupItem> children = new ArrayList<>();

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public ReqAccount getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ReqAccount createdBy) {
		if(createdBy != null) {
			this.createdById = createdBy.getAccountId();
		}
		this.createdBy = createdBy;
	}

	public ReqAccount getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(ReqAccount modifiedBy) {
		if(modifiedBy != null) {
			this.modifiedById = modifiedBy.getAccountId();
		}
		this.modifiedBy = modifiedBy;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReqGroup) {
			ReqGroup a = (ReqGroup)obj ;
			return Objects.equals(this.getGroupId(), a.getGroupId());
		}
		return false;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public Long getId() {
		return this.groupId;
	}

	@Override
	public String getLabel() {
		return this.name;
	}

	@Override
	public List<ReqGroupItem> getChildren() {
		return this.children ;
	}

	public void addItem(ReqGroupItem ... add) {
		this.children.addAll(Arrays.asList(add));
	}
	
	public ReqGroup clone() {  
		ReqGroup o = null;  
        try {  
            o = (ReqGroup) super.clone();  
        } catch (CloneNotSupportedException e) {
			throw new ReqException(e.getMessage(), e);

		}
        return o;  
    } 
	
	public void setChildren(List<ReqGroupItem> children) {
		this.children = children;
	}

}
