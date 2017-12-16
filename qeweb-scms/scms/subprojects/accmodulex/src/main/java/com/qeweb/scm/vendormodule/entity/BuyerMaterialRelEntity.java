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
@Table(name="QEWEB_BUYER_MATERIAL_REL")
public class BuyerMaterialRelEntity extends BaseEntity{
	
	private long buyerId;
	
	private long materialId;
	
	private OrganizationEntity buyer;
	
	private MaterialEntity material;
	
	
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
	
	@Column(name="MATERIAL_ID")
	public long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}
	
	@ManyToOne
	@JoinColumn(name="MATERIAL_ID",insertable=false,updatable=false)
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	

	
	
	

}
