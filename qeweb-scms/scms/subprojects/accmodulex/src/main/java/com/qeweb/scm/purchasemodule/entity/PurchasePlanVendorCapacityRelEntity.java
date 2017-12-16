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
@Table(name = "QEWEB_VENDOR_CAPACITY_REL")
public class PurchasePlanVendorCapacityRelEntity extends BaseEntity {
	
	private OrganizationEntity vendor; //供应商
	private String  capacityCodes;  //产能表的codes
	//非持久化
	private String  capacityNames;  //产能表的names
	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@Column(name="capacity_codes")
	public String getCapacityCodes() {
		return capacityCodes;
	}
	public void setCapacityCodes(String capacityCodes) {
		this.capacityCodes = capacityCodes;
	}
	
	@Transient
	public String getCapacityNames() {
		return capacityNames;
	}
	public void setCapacityNames(String capacityNames) {
		this.capacityNames = capacityNames;
	}

}
