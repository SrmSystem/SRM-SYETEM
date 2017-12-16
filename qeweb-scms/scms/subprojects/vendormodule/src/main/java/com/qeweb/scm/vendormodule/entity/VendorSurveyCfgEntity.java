package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 供应商调查表配置,供应商的调查表跟供应商的阶段关联。
 * 这个配置同样初始化时来源于指定的导航模版
 * @author pjjxiajun
 * @date 2015年5月24日
 * @path com.qeweb.scm.vendormodule.entity.VendorSurveyCfg.java
 */
@Entity
@Table(name = "qeweb_vendor_survey_cfg")
public class VendorSurveyCfgEntity extends IdEntity {
	
	private Long vendorPhasecfgId;
	private Long orgId;
	private Long navTemplateId;
	private Long phaseId;
	private Long surveyTemplateId;
	private Integer surveyTemplateSn;
	private Long surveyDataId;
	private String phaseName;
	private String phaseCode;
	private Integer phaseSn;//配置的阶段顺序,冗余,跟阶段配置里面的保持一致
	private String surveyCode;
	private String surveyName;
	private Integer auditStatus;
	private Integer approveStatus;
	private Integer submitStatus;
	private String auditReason;//当前的审核原因
	private String auditUser;//当前的审核人
	
	private String remark;
	
	/** 级联对象，查看用 */
	private VendorSurveyTemplateEntity surveyTemplate;
	
	private VendorSurveyBaseEntity vendorSurveyBaseEntitys;
	
	public Long getVendorPhasecfgId() {
		return vendorPhasecfgId;
	}
	public void setVendorPhasecfgId(Long vendorPhasecfgId) {
		this.vendorPhasecfgId = vendorPhasecfgId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getNavTemplateId() {
		return navTemplateId;
	}
	public void setNavTemplateId(Long navTemplateId) {
		this.navTemplateId = navTemplateId;
	}
	public Long getPhaseId() {
		return phaseId;
	}
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	public Long getSurveyTemplateId() {
		return surveyTemplateId;
	}
	public void setSurveyTemplateId(Long surveyTemplateId) {
		this.surveyTemplateId = surveyTemplateId;
	}
	public Long getSurveyDataId() {
		return surveyDataId;
	}
	public void setSurveyDataId(Long surveyDataId) {
		this.surveyDataId = surveyDataId;
	}
	public String getPhaseName() {
		return phaseName;
	}
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	public String getPhaseCode() {
		return phaseCode;
	}
	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}
	public String getSurveyCode() {
		return surveyCode;
	}
	public void setSurveyCode(String surveyCode) {
		this.surveyCode = surveyCode;
	}
	public String getSurveyName() {
		return surveyName;
	}
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ManyToOne
	@JoinColumn(name="surveyTemplateId",insertable=false,updatable=false)
	public VendorSurveyTemplateEntity getSurveyTemplate() {
		return surveyTemplate;
	}
	public void setSurveyTemplate(VendorSurveyTemplateEntity surveyTemplate) {
		this.surveyTemplate = surveyTemplate;
	}
	public Integer getPhaseSn() {
		return phaseSn;
	}
	public void setPhaseSn(Integer phaseSn) {
		this.phaseSn = phaseSn;
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
	
	public Integer getSurveyTemplateSn() {
		return surveyTemplateSn;
	}
	public void setSurveyTemplateSn(Integer surveyTemplateSn) {
		this.surveyTemplateSn = surveyTemplateSn;
	}
	
	
	@Transient
	public VendorSurveyBaseEntity getVendorSurveyBaseEntitys() {
		return vendorSurveyBaseEntitys;
	}
	public void setVendorSurveyBaseEntitys(
			VendorSurveyBaseEntity vendorSurveyBaseEntitys) {
		this.vendorSurveyBaseEntitys = vendorSurveyBaseEntitys;
	}
	
	
	

}
