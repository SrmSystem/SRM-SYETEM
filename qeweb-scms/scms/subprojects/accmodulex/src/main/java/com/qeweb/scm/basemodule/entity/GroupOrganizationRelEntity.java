package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 采购组和采购组织关系表
 * @author eleven
 * @date 2017年5月17日
 * @path com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity.java
 */
@Entity
@Table(name="QEWEB_GROUP_ORGANIZATION")
public class GroupOrganizationRelEntity extends IdEntity {
	
    /**
     * 采购组的id
     */
	private Long groupId;
	
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
	private PurchasingGroupEntity group;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
	@JoinColumn(name="groupId",insertable=false,updatable=false)
	public PurchasingGroupEntity getGroup() {
		return group;
	}
	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}

}
