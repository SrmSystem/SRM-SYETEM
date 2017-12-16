package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 采购组织供应商关系
 * @author hp-pc
 *
 */
@Entity
@Table(name="QEWEB_BUYER_VENDOR_REL")
public class BuyerVendorRelEntity extends BaseEntity{
	
	private long buyerId;
	
	private long vendorId;
	
	private OrganizationEntity buyer;
	
	private OrganizationEntity vendor;
	
	
	@Column(name="BUYER_ID")
	public long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(long buyerId) {
		this.buyerId = buyerId;
	}
	
	@Column(name="VENDOR_ID")
	public long getVendorId() {
		return vendorId;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	
	@ManyToOne
	@JoinColumn(name="VENDOR_ID",insertable=false,updatable=false)
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@ManyToOne
	@JoinColumn(name="BUYER_ID",insertable=false,updatable=false)
	public OrganizationEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}

	
	
	

}
