package com.haleywang.monitor.entity;

import com.haleywang.monitor.common.ReqException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqGroup implements Serializable , ReqGroupItem  {

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
	private transient List<ReqGroupItem> children = new ArrayList<>();

    public ReqGroup(ReqGroup reqGroup) {
		try {
			BeanUtils.copyProperties(this, reqGroup);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ReqException(e);
		}
	}

    public void setCreatedBy(ReqAccount createdBy) {
		if(createdBy != null) {
			this.createdById = createdBy.getAccountId();
		}
		this.createdBy = createdBy;
	}

	public void setModifiedBy(ReqAccount modifiedBy) {
		if(modifiedBy != null) {
			this.modifiedById = modifiedBy.getAccountId();
		}
		this.modifiedBy = modifiedBy;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReqGroup) {
			ReqGroup a = (ReqGroup)obj ;
			return Objects.equals(this.getGroupId(), a.getGroupId());
		}
		return false;
	}


	@Override
	public int hashCode() {
		return super.hashCode();
	}


	public void addItem(ReqGroupItem ... add) {
		this.children.addAll(Arrays.asList(add));
	}
	


	@Override
	public Long getId() {
		return groupId;
	}

	@Override
	public String getLabel() {
		return name;
	}

	public boolean isGroup() {
		return true;
	}
}
