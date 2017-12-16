package com.qeweb.scm.baseline.common.entity;

import javax.persistence.MappedSuperclass;

import com.qeweb.scm.basemodule.entity.BaseEntity;
@MappedSuperclass
public class BaseAuditEntity extends BaseEntity{
	
	public static final Integer auditStauts_no = 0;
	public static final Integer auditStauts_submit = 1;
	public static final Integer auditStauts_pass = 2;
	public static final Integer auditStauts_reject = -1;

	private Integer auditStatus;       //审核状态    0:未审核   1：提交审核   2：审核通过   -1：审核驳回
	private String currentAuditUser;   //当前审核人
	private String processIns;         //对应的流程实例
	
	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getCurrentAuditUser() {
		return currentAuditUser;
	}

	public void setCurrentAuditUser(String currentAuditUser) {
		this.currentAuditUser = currentAuditUser;
	}

	public String getProcessIns() {
		return processIns;
	}

	public void setProcessIns(String processIns) {
		this.processIns = processIns;
	}
	
}
