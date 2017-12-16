package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "qeweb_role")
public class RoleEntity extends BaseEntity{
	
	private String code;		//角色编码
	private String name;		//角色名称
	private String roleType;	//角色类型
	private String remark;		//备注
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
}
