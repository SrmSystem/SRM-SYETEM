package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色数据权限
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_role_data")
public class RoleDataEntity extends IdEntity {

	private String dataIds; 	// 数据权限IDs
	private long roleDataCfgId; // 角色数据权限配置ID
	private long roleId; 		// 角色ID
	private String remark; 		// 备注

	public String getDataIds() {
		return dataIds;
	}

	public void setDataIds(String dataIds) {
		this.dataIds = dataIds;
	}

	public long getRoleDataCfgId() {
		return roleDataCfgId;
	}

	public void setRoleDataCfgId(long roleDataCfgId) {
		this.roleDataCfgId = roleDataCfgId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
