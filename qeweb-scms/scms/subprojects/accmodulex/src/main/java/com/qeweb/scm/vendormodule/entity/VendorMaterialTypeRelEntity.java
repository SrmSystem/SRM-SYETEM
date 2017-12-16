package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@Entity
@Table(name="QEWEB_VENDOR_MATTYPE_REL")
public class VendorMaterialTypeRelEntity extends BaseEntity{
	
	private long materialTypeId;
	
	private String materialTypeName;
	
	private String materialTypeCode;
	
	private long orgId;
	
	private long vendorId;
	
	private String orgCode;
	
	private String orgName;
	
	private String topMaterialTypeName;
	
	private OrganizationEntity vendor;
	
	private MaterialTypeEntity materialType;
	
	@ManyToOne
	@JoinColumn(name="ORG_ID",insertable=false,updatable=false)
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@ManyToOne
	@JoinColumn(name="MATERIAL_TYPE_ID",insertable=false,updatable=false)
	public MaterialTypeEntity getMaterialType() {
		return materialType;
	}
	public void setMaterialType(MaterialTypeEntity materialType) {
		this.materialType = materialType;
	}
	
	@Column(name="MATERIAL_TYPE_ID")
	public long getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	public String getMaterialTypeName() {
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}

	public String getMaterialTypeCode() {
		return materialTypeCode;
	}

	public void setMaterialTypeCode(String materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
	}

	
	@Column(name="ORG_ID")
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTopMaterialTypeName() {
		return topMaterialTypeName;
	}
	public void setTopMaterialTypeName(String topMaterialTypeName) {
		this.topMaterialTypeName = topMaterialTypeName;
	}
	
	

}
