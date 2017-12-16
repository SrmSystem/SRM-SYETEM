package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@MappedSuperclass
public class EPBaseEntity extends BaseEntity{
	
	
	private Long buyerId;
	
	private OrganizationEntity buyer;

	@Column(name="BUYER_ID")
	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
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
	

	
}
