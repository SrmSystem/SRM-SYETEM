package com.qeweb.scm.purchasemodule.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "QEWEB_MATERIAL_PLAN")
public class PurchaseMaterialPlanEntity extends BaseEntity {
	
	private Long materialId;
	
	private MaterialEntity material;
	
	private Double planNum;
	
	private Timestamp planTime;
	
	private Integer publishStatus;
	
	private Timestamp publishTime;
	
	private Long publishUserId;
	
	private UserEntity publishUser;
	
	private Long buyerId;
	
	private OrganizationEntity buyer;
	
	private  String vendorCode;
	
	private  String vendorName;
	
	private   String  orderCode;
	
	//冗余
	private String tableDatas;
	
	private String materialCode;
	
	private String materialName;

	
	@Column(name="MATERIAL_ID")
	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
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

	public Double getPlanNum() {
		return planNum;
	}

	public void setPlanNum(Double planNum) {
		this.planNum = planNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Timestamp planTime) {
		this.planTime = planTime;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	@Column(name="PUBLISH_USER_ID")
	public Long getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(Long publishUserId) {
		this.publishUserId = publishUserId;
	}

	@ManyToOne
	@JoinColumn(name="PUBLISH_USER_ID",insertable=false,updatable=false)
	public UserEntity getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(UserEntity publishUser) {
		this.publishUser = publishUser;
	}

	@Column(name="BUYER_ID")
	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	@Transient
	public String getTableDatas() {
		return tableDatas;
	}
	public void setTableDatas(String tableDatas) {
		this.tableDatas = tableDatas;
	}
	
	
	
	
	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

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
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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
