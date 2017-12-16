package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_vendor_notice_cfg")
public class NoticeVendorCFGEntity extends IdEntity {

	private Long noticeId;
	
	private Long orgId;
	
	private Long userId;
	
	private Long roleId; 
	
	private Integer ntype;
	
	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Integer getNtype() {
		return ntype;
	}

	public void setNtype(Integer ntype) {
		this.ntype = ntype;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
