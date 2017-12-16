package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;


@Entity
@Table(name="QEWEB_BUYER_MATERIAL_TYPE_REL")
public class BuyerMaterialTypeRelEntity extends BaseEntity{
	
	private long buyerId;
	
	private long materialTypeId;
	
	private OrganizationEntity buyer;
	
	private MaterialTypeEntity materialType;
	
	
	@Column(name="BUYER_ID")
	public long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(long buyerId) {
		this.buyerId = buyerId;
	}
	
	
	@ManyToOne
	@JoinColumn(name="BUYER_ID",insertable=false,updatable=false)
	public OrganizationEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}
	
	@Column(name="MATERIAL_TYPE_ID")
	public long getMaterialTypeId() {
		return materialTypeId;
	}
	public void setMaterialTypeId(long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}
	
	@ManyToOne
	@JoinColumn(name="MATERIAL_TYPE_ID",insertable=false,updatable=false)
	public MaterialTypeEntity getMaterialType() {
		return materialType;
	}
	public void setMaterialType(MaterialTypeEntity materialType) {
		this.materialType = materialType;
	}
	
	
	

	
	

	
	
	

}
