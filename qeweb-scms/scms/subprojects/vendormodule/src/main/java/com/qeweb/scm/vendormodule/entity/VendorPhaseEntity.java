package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商阶段定义
 * @author pjjxiajun
 * @date 2015年5月5日
 * @path com.qeweb.scm.vendormodule.entity.VedorPhaseEntity.java
 */
@Entity
@Table(name = "QEWEB_VENDOR_PHASE")
public class VendorPhaseEntity extends BaseEntity{
	
	private String code;
	private String name;
	private Long roleId;
	private String roleName;
	private String roleCode;
	private String remark;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	
	

}
