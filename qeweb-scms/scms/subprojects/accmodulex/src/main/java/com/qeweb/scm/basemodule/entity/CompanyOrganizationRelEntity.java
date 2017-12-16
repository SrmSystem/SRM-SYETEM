package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 公司和采购组织的关系表
 * @author ELEVEN
 * @date 2017年5月10日
 * @path com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity.java
 */
@Entity
@Table(name="QEWEB_COMPANY_ORGANIZATION")
public class CompanyOrganizationRelEntity extends IdEntity {
    /**
     * 公司的id
     */
	private Long companyId;
	
    /**
     * 采购组织的id
     */
	private Long organizationId;
	
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
	private OrganizationEntity organizationEntity;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	@JoinColumn(name="organizationId",insertable=false,updatable=false)
	public OrganizationEntity getOrganizationEntity() {
		return organizationEntity;
	}
	public void setOrganizationEntity(OrganizationEntity organizationEntity) {
		this.organizationEntity = organizationEntity;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}
	
	
	
	
	
}
