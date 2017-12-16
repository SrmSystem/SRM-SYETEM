package com.qeweb.scm.basemodule.entity;


import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 采购组的相关信息
 * @author eleven
 * @date 2017年5月10日
 * @path com.qeweb.scm.basemodule.entity.CompanyEntity.java
 */
@Entity
@Table(name="QEWEB_PURCHASING_GROUP")
public class PurchasingGroupEntity extends BaseEntity {

    /**
     * 采购组名称
     */
	private String name;
	
    /**
     * 采购组代码
     */
	private String code;
	
	/**
     * 采购组注释
     */
	private String remark;
	
	
	//非持久化
	private String factoryId;//工厂
	private String orgId;//采购组织
	private Integer isCheck;//是否够勾选
	
	
	
	
	@Transient
	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
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
	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
	@Transient
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
