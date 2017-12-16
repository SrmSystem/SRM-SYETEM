package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="QEWEB_ROLE_VIEW")
public class RoleViewEntity extends IdEntity{
	
	private long roleId;
	private long viewId;
	private long viewPid;
	private int viewType;
	private String operations;
	private String remark;
	private ViewEntity view;
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public long getViewId() {
		return viewId;
	}
	public void setViewId(long viewId) {
		this.viewId = viewId;
	}
	public int getViewType() {
		return viewType;
	}
	public void setViewType(int viewType) {
		this.viewType = viewType;
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
	public long getViewPid() {
		return viewPid;
	}
	public void setViewPid(long viewPid) {
		this.viewPid = viewPid;
	}
	
	@ManyToOne
	@JoinColumn(name="viewId",insertable=false,updatable=false)
	public ViewEntity getView() {
		return view;
	}
	public void setView(ViewEntity view) {
		this.view = view;
	}
	
	
	
	

}
