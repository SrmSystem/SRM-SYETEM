package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "QEWEB_PURCHASE_PLAN_HEAD")
public class PurchasePlanHeadEntity extends BaseEntity {

	private PurchasePlanItemEntity planItem;
	
	private String headerName;
	
	private String headerValues;
	
	private String versionNumber;
	
	private Integer isNew;
	
	
	public String getHeaderName() {
		return headerName;
	}

	@ManyToOne
	@JoinColumn(name="plan_Item_Id")
	public PurchasePlanItemEntity getPlanItem() {
		return planItem;
	}

	public void setPlanItem(PurchasePlanItemEntity planItem) {
		this.planItem = planItem;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getHeaderValues() {
		return headerValues;
	}

	public void setHeaderValues(String headerValues) {
		this.headerValues = headerValues;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
}
