package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 工厂和采购组织的关系表
 * @author eleven
 * @date 2017年5月17日
 * @path com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity.java
 */
@Entity
@Table(name="QEWEB_FACTORY_ORGANIZATION")
public class FactoryOrganizationRelEntity extends IdEntity {
	
	/**
     * 工厂id
     */
	private Long factoryId;
	
	/**
     * 采购组织的id
     */
	private Long orgId;
	
	/**
     * 描述
     */
	private String remark;
	
	
	/**
     * 废除
     */
	private Integer abolished;
	
	/** 非持久化字段 */
	private OrganizationEntity org;
	private FactoryEntity factory;
	
	//非持久化字段
	private String orgCode;
	private String orgName;
	private String factoryCode;
	private String factoryName;
	
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@JoinColumn(name="orgId",insertable=false,updatable=false)
	public OrganizationEntity getOrg() {
		return org;
	}
	public void setOrg(OrganizationEntity org) {
		this.org = org;
	}
	
	@ManyToOne
	@JoinColumn(name="factoryId",insertable=false,updatable=false)
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	@Transient
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Transient
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@Transient
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	@Transient
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}

	
	
	
	
}
