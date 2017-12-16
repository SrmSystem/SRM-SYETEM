package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.qeweb.scm.basemodule.entity.BaseEntity;


@Entity
@Table(name = "QEWEB_PURCHASE_TOTAL_PLAN_HEAD")
public class PurchaseTotalPlanHeadEntity extends BaseEntity {

	private PurchaseTotalPlanItemEntity planItem;
	
	private String headerName;
	
	private String headerValues;
	
	private String versionNumber;
	
	private Integer isNew;
	
	
	public String getHeaderName() {
		return headerName;
	}
	
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	@ManyToOne
	@JoinColumn(name="plan_Item_Id")
	public PurchaseTotalPlanItemEntity getPlanItem() {
		return planItem;
	}

	public void setPlanItem(PurchaseTotalPlanItemEntity planItem) {
		this.planItem = planItem;
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
