package com.qeweb.scm.basemodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="qeweb_factory")
public class FactoryEntity extends BaseEntity {
	private String name;
	private String code;//工厂+品牌
	private Long brandId;
	private Long orgId;
	private String remark;
	
	/** 非持久化数据 */
	private List<FactoryBrandRelEntity> factoryBrandList;


	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Transient
	public List<FactoryBrandRelEntity> getFactoryBrandList() {
		return factoryBrandList;
	}
	public void setFactoryBrandList(List<FactoryBrandRelEntity> factoryBrandList) {
		this.factoryBrandList = factoryBrandList;
	}
	
	
	
	
	
	
}
