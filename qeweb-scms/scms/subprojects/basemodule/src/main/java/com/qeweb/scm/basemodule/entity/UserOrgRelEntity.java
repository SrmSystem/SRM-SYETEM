package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户和组织的对应表
 * 如果系统要求有多对多的要求，启用这个类
 * @author pjjxiajun
 * @date 2015年3月13日
 * @path com.qeweb.scm.basemodule.entity.com.qeweb.scm.basemodule.entity
 */
@Entity
@Table(name="qeweb_user_org")
public class UserOrgRelEntity extends IdEntity{
	
	private UserEntity user;
	private OrganizationEntity org;
	private Integer orgType;
	private Integer orgRoleType;
	private String orgName;
	@ManyToOne
	@JoinColumn(name="user_id")
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	@ManyToOne
	@JoinColumn(name="org_id")
	public OrganizationEntity getOrg() {
		return org;
	}
	public void setOrg(OrganizationEntity org) {
		this.org = org;
	}
	public Integer getOrgType() {
		return orgType;
	}
	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}
	public Integer getOrgRoleType() {
		return orgRoleType;
	}
	public void setOrgRoleType(Integer orgRoleType) {
		this.orgRoleType = orgRoleType;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}
