package com.qeweb.scm.vendormodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商模版和阶段关系
 * @author pjjxiajun
 * @date 2015年5月5日
 * @path com.qeweb.scm.vendormodule.entity.VendorTemplatePhaseEntity.java
 */
@Entity
@Table(name = "QEWEB_VENDOR_TEMPLATE_PHASE")
public class VendorTemplatePhaseEntity extends BaseEntity{
	
	private VendorNavTemplateEntity vendorNavTemplate;
	private VendorPhaseEntity vendorPhase;
	private Long templateId;
	private Long phaseId;
	private String code;
	private String name;
	private Integer phaseSn;
	private String remark;
	
	//非持久化字段用来页面传值
	private String surveyIds;
	private List<VendorTemplateSurveyEntity> itemList;//配置的调查表集合
	
	@ManyToOne
	@JoinColumn(name = "templateId",insertable=false,updatable=false)
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	
	@Transient
	public String getSurveyIds() {
		return surveyIds;
	}

	public void setSurveyIds(String surveyIds) {
		this.surveyIds = surveyIds;
	}

	@Transient
	public List<VendorTemplateSurveyEntity> getItemList() {
		return itemList;
	}

	public void setItemList(List<VendorTemplateSurveyEntity> itemList) {
		this.itemList = itemList;
	}
	
	
	
	
	
}
