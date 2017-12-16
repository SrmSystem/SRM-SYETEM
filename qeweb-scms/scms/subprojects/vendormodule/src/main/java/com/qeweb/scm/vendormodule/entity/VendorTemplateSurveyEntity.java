package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商导航模版和调查表模版的关系，其实是和模版的阶段进行的对应，也就是
 * 哪个阶段对应了哪些调查表
 * @author pjjxiajun
 * @date 2015年5月28日
 * @path com.qeweb.scm.vendormodule.entity.VendorTemplateSurveyEntity.java
 */
@Entity
@Table(name = "QEWEB_VENDOR_TEMPLATE_SURVEY")
public class VendorTemplateSurveyEntity extends BaseEntity{
	
	private VendorNavTemplateEntity vendorNavTemplate;
	private VendorPhaseEntity vendorPhase;
	private Long vendorTemplateId;
	private Long phaseId;
	private Long surveyTemplateId;//调查表模版ID
	private Integer surveyTemplateSn;//调查表模版顺序
	private Long templatePhaseId;//模版阶段的ID
	private String phaseCode;
	private String phaseName;
	private Integer phaseSn;
	private String surveyName;//调查表名称
	private String surveyCode;//调查表编码
	private String remark;
	
	//非持久化字段用来页面传值
	private String surveyIds;
	
	@ManyToOne
	@JoinColumn(name = "vendorTemplateId",insertable=false,updatable=false)
	public VendorNavTemplateEntity getVendorNavTemplate() {
		return vendorNavTemplate;
	}
	
	public void setVendorNavTemplate(VendorNavTemplateEntity vendorNavTemplate) {
		this.vendorNavTemplate = vendorNavTemplate;
	}
	
	@ManyToOne
	@JoinColumn(name = "phaseId",insertable=false,updatable=false)
	public VendorPhaseEntity getVendorPhase() {
		return vendorPhase;
	}
	public void setVendorPhase(VendorPhaseEntity vendorPhase) {
		this.vendorPhase = vendorPhase;
	}
	public Integer getPhaseSn() {
		return phaseSn;
	}
	public void setPhaseSn(Integer phaseSn) {
		this.phaseSn = phaseSn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	
	public Long getVendorTemplateId() {
		return vendorTemplateId;
	}

	public void setVendorTemplateId(Long vendorTemplateId) {
		this.vendorTemplateId = vendorTemplateId;
	}

	public Long getSurveyTemplateId() {
		return surveyTemplateId;
	}

	public void setSurveyTemplateId(Long surveyTemplateId) {
		this.surveyTemplateId = surveyTemplateId;
	}

	public Long getTemplatePhaseId() {
		return templatePhaseId;
	}

	public void setTemplatePhaseId(Long templatePhaseId) {
		this.templatePhaseId = templatePhaseId;
	}

	public String getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	@Transient
	public String getSurveyIds() {
		return surveyIds;
	}

	public void setSurveyIds(String surveyIds) {
		this.surveyIds = surveyIds;
	}

	public String getSurveyCode() {
		return surveyCode;
	}

	public void setSurveyCode(String surveyCode) {
		this.surveyCode = surveyCode;
	}

	public Integer getSurveyTemplateSn() {
		return surveyTemplateSn;
	}

	public void setSurveyTemplateSn(Integer surveyTemplateSn) {
		this.surveyTemplateSn = surveyTemplateSn;
	}
	
	
	
	
	
	
	
	
}
