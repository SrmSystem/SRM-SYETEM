package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 公司和工厂的关系表
 * @author ELEVEN
 * @date 2017年5月10日
 * @path com.qeweb.scm.basemodule.entity.CompanyFactoryRelEntity.java
 */
@Entity
@Table(name="QEWEB_COMPANY_FACTORY")
public class CompanyFactoryRelEntity extends IdEntity {
    /**
     * 公司的id
     */
	private Long companyId;
	
    /**
     * 工厂的id
     */
	private Long factoryId;
	
    /**
     * 描述
     */
	private String remark;
	
	/**
     * 废除
     */
	private Integer abolished;
	
	/** 非持久化字段 */
	private CompanyEntity company;
	private FactoryEntity factory;
	
	//非持久化字段
	private String companyCode;
	private String companyName;
	private String factoryCode;
	private String factoryName;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@JoinColumn(name="companyId",insertable=false,updatable=false)
	public CompanyEntity getCompany() {
		return company;
	}
	public void setCompany(CompanyEntity company) {
		this.company = company;
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
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	@Transient
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
