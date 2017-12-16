package com.qeweb.scm.vendormodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "qeweb_vendor_survey_base")
public class VendorSurveyBaseEntity extends BaseEntity{
	
	private long vendorCfgId;
	private long vendorId;
	private long orgId;
	private long templateId;
	private String templatePath;
	private String templateCode;
	
	private Integer versionNO;//版本号，记录版本号，编辑时，一定是最新版本
	private Integer currentVersion;//是否为当前版本。第一个版本和最新的通过审核的版本才是当前版本
	private Integer auditStatus;//审核状态
	private Integer approveStatus;//审批状态
	private Integer submitStatus;//提交状态
	private String auditReason;//审核原因
	private String auditUser;//审核人
	
	//非持久化字段
	private List<VendorSurveyDataEntity> itemList;
	private Integer jdJuct;
	
	@Transient
	public Integer getJdJuct() {
		return jdJuct;
	}
	public void setJdJuct(Integer jdJuct) {
		this.jdJuct = jdJuct;
	}
	@Transient
	public List<VendorSurveyDataEntity> getItemList() {
		return itemList;
	}
	public void setItemList(List<VendorSurveyDataEntity> itemList) {
		this.itemList = itemList;
	}
	public long getVendorCfgId() {
		return vendorCfgId;
	}
	public void setVendorCfgId(long vendorCfgId) {
		this.vendorCfgId = vendorCfgId;
	}
	public long getVendorId() {
		return vendorId;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public String getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public Integer getVersionNO() {
		return versionNO;
	}
	public void setVersionNO(Integer versionNO) {
		this.versionNO = versionNO;
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
	public Integer getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}
	public Integer getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	
	
	

}
