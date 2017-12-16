package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * 工厂和品牌的关系表
 * @author pjjxiajun
 * @date 2015年6月18日
 * @path com.qeweb.scm.basemodule.entity.FactoryBrandRelEntity.java
 */
@Entity
@Table(name="qeweb_factory_brand")
public class FactoryBrandRelEntity extends IdEntity {
	private Long factoryId;
	private Long brandId;
	private String remark;
	
	/** 非持久化字段 */
	private BussinessRangeEntity brand;
	private FactoryEntity factory;
	
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@JoinColumn(name="brandId",insertable=false,updatable=false)
	public BussinessRangeEntity getBrand() {
		return brand;
	}
	public void setBrand(BussinessRangeEntity brand) {
		this.brand = brand;
	}


	
}
