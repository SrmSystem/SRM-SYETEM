package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 角色数据权限配置
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_role_data_cfg")
public class RoleDataCfgEntity extends IdEntity {

	private String dataCode; 		// 数据权限编码
	private String dataName; 		// 数据权限名称
	private String dataClazz; 		// 数据权限类
	private String dataScope; 		// 数据权限范围
	private String remark; 			// 备注
	
	//非持久化
	private long roleId;

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataClazz() {
		return dataClazz;
	}

	public void setDataClazz(String dataClazz) {
		this.dataClazz = dataClazz;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

}
