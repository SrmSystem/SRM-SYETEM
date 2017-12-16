package com.qeweb.scm.purchasemodule.entity;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;



@Entity
@Table(name = "QEWEB_PURCHASE_TOTAL_PLAN")
public class PurchaseTotalPlanEntity extends BaseEntity {
	
	private String month; //大版本号
	private Set<PurchaseTotalPlanItemEntity> planItem;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="plan") 
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<PurchaseTotalPlanItemEntity> getPlanItem() {
		return planItem;
	}
	public void setPlanItem(Set<PurchaseTotalPlanItemEntity> planItem) {
		this.planItem = planItem;
	}
}
