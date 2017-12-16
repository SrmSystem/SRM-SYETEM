package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

//@Entity
//@Table(name = "qeweb_permission")
public class PermissionEntity extends BaseEntity {
	
	/** 权限主体（人，角色，部门） */
	private String permissionMaster;
	/** 权限对象（对象的类名） */
	private String permissionObj;
	/** 权限主体的主键 */
	private Long permissionMasterId;
	/** 被访问的类型（page,menu,button） */
	private String accessType;
	/** 被访问的对象（对象的类名） */
	private String accessObj;
	/** 被访问对象的主键 */
	private Long accessId;
	/** 拥有的操作权限 */
	private String operations;
	private String remark;
	
	public String getPermissionMaster() {
		return permissionMaster;
	}
	public void setPermissionMaster(String permissionMaster) {
		this.permissionMaster = permissionMaster;
	}
	public String getPermissionObj() {
		return permissionObj;
	}
	public void setPermissionObj(String permissionObj) {
		this.permissionObj = permissionObj;
	}
	public Long getPermissionMasterId() {
		return permissionMasterId;
	}
	public void setPermissionMasterId(Long permissionMasterId) {
		this.permissionMasterId = permissionMasterId;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getAccessObj() {
		return accessObj;
	}
	public void setAccessObj(String accessObj) {
		this.accessObj = accessObj;
	}
	public Long getAccessId() {
		return accessId;
	}
	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}
	public String getOperations() {
		return operations;
	}
	public void setOperations(String operations) {
		this.operations = operations;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}
