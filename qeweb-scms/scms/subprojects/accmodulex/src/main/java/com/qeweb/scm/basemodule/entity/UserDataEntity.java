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
@Table(name = "qeweb_user_data")
public class UserDataEntity extends IdEntity {

	private String dataIds; 	// 数据权限IDs
	private long roleDataCfgId; // 角色数据权限配置ID
	private long userId; 		// 用户ID
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
