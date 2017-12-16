package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 供应商-品牌-工厂-物料分类关系
 * @author pjjxiajun
 * @date 2015年11月4日
 * @path com.qeweb.scm.basemodule.entity.VendorBFMatTypeEntity.java
 */
@Entity
@Table(name="QEWEB_VEN_B_F_MATTYPE")
public class VendorBFMatTypeEntity extends IdEntity{
	private Long orgId;
	private String orgName;
	private String orgCode;
	private Long brandId;
	private String brandName;
	private String brandCode;
	private Long factoryId;
	private String factoryName;
	private String factoryCode;
	private Long matTypeId;
	private String matTypeCode;
	private String matTypeName;
	
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Long getMatTypeId() {
		return matTypeId;
	}
	public void setMatTypeId(Long matTypeId) {
		this.matTypeId = matTypeId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getMatTypeCode() {
		return matTypeCode;
	}
	public void setMatTypeCode(String matTypeCode) {
		this.matTypeCode = matTypeCode;
	}
	public String getMatTypeName() {
		return matTypeName;
	}
	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}
	
	
	
	

}
