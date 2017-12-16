package com.qeweb.scm.qualityassurance.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 二方审核管理
 * @author SXL
 *
 */
@Entity
@Table(name = "qeweb_qm_csolution")
public class TwoAuditSolutionEntity extends BaseEntity{

	private Long twoauditId;
	
	private String solutionContent;//整改方案
	
	private String fileUrl;//附件地址
	
	private Integer currentVersion;//是否为当前版本。第一个版本和最新的通过审核的版本才是当前版本
	
	private Integer auditStatus;//审核状态
	
	private String auditReason;//审核原因

	private TwoAuditEntity twoAuditEntity;

	public Long getTwoauditId() {
		return twoauditId;
	}

	public void setTwoauditId(Long twoauditId) {
		this.twoauditId = twoauditId;
	}

	public String getSolutionContent() {
		return solutionContent;
	}

	public void setSolutionContent(String solutionContent) {
		this.solutionContent = solutionContent;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(Integer currentVersion) {
		this.currentVersion = currentVersion;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	@ManyToOne
	@JoinColumn(name="twoauditId",insertable=false,updatable=false)
	public TwoAuditEntity getTwoAuditEntity() {
		return twoAuditEntity;
	}

	public void setTwoAuditEntity(TwoAuditEntity twoAuditEntity) {
		this.twoAuditEntity = twoAuditEntity;
	}
}
