package com.qeweb.scm.purchasemodule.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;



import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@Entity
@Table(name = "QEWEB_MATERIAL_VENDOR_PLAN")
public class PurchaseMaterialVendorPlanEntity extends BaseEntity {
	
	
	private PurchaseMaterialPlanEntity plan;

	private Double planNum;
	
	
	private OrganizationEntity vendor;
	
	private String orderCode;
	
	//冗余
	private String tableDatas;
	
	private String vendorCode;
	
	private String vendorName;
	
	private String materialCode;
	
	private String planTime;
	
	private Long vendorId;
	
	
	

	
	@ManyToOne
	@JoinColumn(name="PLAN_ID")
	public PurchaseMaterialPlanEntity getPlan() {
		return plan;
	}
	public void setPlan(PurchaseMaterialPlanEntity plan) {
		this.plan = plan;
	}
	

	
	@ManyToOne
	@JoinColumn(name="VENDOR_ID")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	public Double getPlanNum() {
		return planNum;
	}
	public void setPlanNum(Double planNum) {
		this.planNum = planNum;
	}

	
	
	
	@Transient
	public String getTableDatas() {
		return tableDatas;
	}
	public void setTableDatas(String tableDatas) {
		this.tableDatas = tableDatas;
	}
	
	@Transient
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	@Transient
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@Transient
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Transient
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	
	@Transient
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	
	
	
	
	
	
	
	
	
	


	
	
	
	
	
	
	

	
	
	
	
	
	

	

}
