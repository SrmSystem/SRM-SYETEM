package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;

@Entity
@Table(name = "qeweb_user")
public class UserEntity extends BaseEntity {
	private String loginName;
	private String name;
	private String password;
	private String salt;
	private String roles;
	private String mobile;
	private String email;
	private Integer enabledStatus;
	private Timestamp registerDate;
	private Long companyId;
	private Long departmentId;
	private OrganizationEntity company;
	private OrganizationEntity department;
	
	
	// 非数据库字段
	private String roleName;  	// 角色名称
	private Long buyerId;		// 采购ID
	
	public UserEntity() {
		
	}
	
	public UserEntity(long id) {
		setId(id); 
	}
	
	public UserEntity(long id, Long companyId) {
		setId(id); 
		setCompanyId(companyId); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
	public Timestamp getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(Integer enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	@ManyToOne
	@JoinColumn(name="COMPANY_ID",insertable=false,updatable=false)
	public OrganizationEntity getCompany() {
		return company;
	}

	public void setCompany(OrganizationEntity company) {
		this.company = company;
	}

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID",insertable=false,updatable=false)
	public OrganizationEntity getDepartment() {
		return department;
	}

	public void setDepartment(OrganizationEntity department) {
		this.department = department;
	}
	
	
    @Column(name="COMPANY_ID")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	 @Column(name="DEPARTMENT_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Transient
	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	@Transient
	@JsonIgnore
	public List<String> getRoleList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		if(StringUtils.isEmpty(roles))
			return new ArrayList<String>();
		
		return ImmutableList.copyOf(StringUtils.split(roles, ","));
	}

}
