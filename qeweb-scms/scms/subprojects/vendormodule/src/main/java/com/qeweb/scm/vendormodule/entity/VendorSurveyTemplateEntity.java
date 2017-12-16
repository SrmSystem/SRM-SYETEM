package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "QEWEB_VENDOR_SURVEY_TEMPLATE")
public class VendorSurveyTemplateEntity extends BaseEntity {
	
	private String code;
	private String name;
	private Integer templateType;
	private String beanId;
	private String path;
	private String fileName;
	private Integer sn;
	private String remark;
	public String getCode() {
		return code;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
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
	public Integer getTemplateType() {
		return templateType;
	}
	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
