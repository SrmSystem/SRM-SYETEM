package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 模版设置
 * @author pjjxiajun
 * @date 2015年8月15日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity.java
 */
@Entity
@Table(name="qeweb_assess_template_setting")
public class VendorPerforTemplateSettingEntity  extends IdEntity{
	
	private Integer weightType;//模版值类型
	private Long templateId;//模版ID
	private Long sourceId;//来源ID，目前只有维度和指标ID
	private Integer sourceType;//来源类型，指标或维度 //模版类型 @PerformanceTypeConstant
	private String name;//来源名称
	private Double weightNumber;//权重数
	private String formula;//公式
	private String remark;//备注
	private Long parentId;//父级ID-指向sourceId
	private Integer parentType;//父级的类型
	private Integer enableStatus;//启用状态
	
	/* 非数据库库字段 */
	private Long _parentId;//父级ID，主要用作UI使用
	private VendorPerforTemplateEntity template;//关联模版
	public Integer getWeightType() {
		return weightType;
	}
	public void setWeightType(Integer weightType) {
		this.weightType = weightType;
	}
	public Double getWeightNumber() {
		return weightNumber;
	}
	public void setWeightNumber(Double weightNumber) {
		this.weightNumber = weightNumber;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	
	public Integer getParentType() {
		return parentType;
	}
	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}
	@Transient
	public Long get_parentId() {
		if(_parentId==null){
			_parentId = getParentId();
		}
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	@ManyToOne
	@JoinColumn(name="templateId",insertable=false,updatable=false)
	public VendorPerforTemplateEntity getTemplate() {
		return template;
	}
	public void setTemplate(VendorPerforTemplateEntity template) {
		this.template = template;
	}
	
	
}
