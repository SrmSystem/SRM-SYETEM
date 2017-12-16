package com.qeweb.scm.vendorperformancemodule.entity;



import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 整改方案
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_csolution")
public class VendorPerforCorrectionSolutionEntity  extends BaseEntity{
	
	private Long correctionId;
	
	private String solutionContent;//整改方案
	
	private String fileUrl;//附件地址
	
	private Integer currentVersion;//是否为当前版本。第一个版本和最新的通过审核的版本才是当前版本
	
	private Integer auditStatus;//审核状态
	
	private String auditReason;//审核原因
	
	private VendorPerforCorrectionEntity correctionEntity;

	public Long getCorrectionId() {
		return correctionId;
	}

	public void setCorrectionId(Long correctionId) {
		this.correctionId = correctionId;
	}

	public String getSolutionContent() {
		return solutionContent;
	}

	public void setSolutionContent(String solutionContent) {
		this.solutionContent = solutionContent;
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
	@JoinColumn(name="correctionId",insertable=false,updatable=false)
	public VendorPerforCorrectionEntity getCorrectionEntity() {
		return correctionEntity;
	}

	public void setCorrectionEntity(VendorPerforCorrectionEntity correctionEntity) {
		this.correctionEntity = correctionEntity;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
