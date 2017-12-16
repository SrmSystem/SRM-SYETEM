package com.qeweb.scm.vendormodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商导航模版
 * @author pjjxiajun
 * @date 2015年5月5日
 * @path com.qeweb.scm.vendormodule.entity.VendorNavTemplateEntity.java
 */
@Entity
@Table(name = "QEWEB_VENDOR_NAV_TEMPLATE")
public class VendorNavTemplateEntity extends BaseEntity{
	private String code;
	private String name;
	private Integer defaultFlag;
	private Integer rangeType;
	private Integer finishStatus;
	private String remark;
	
	private List<VendorTemplatePhaseEntity> phaseList;
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
	public Integer getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getRangeType() {
		return rangeType;
	}
	public void setRangeType(Integer rangeType) {
		this.rangeType = rangeType;
	}
	public Integer getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}
	
	@Transient
	public List<VendorTemplatePhaseEntity> getPhaseList() {
		return phaseList;
	}
	public void setPhaseList(List<VendorTemplatePhaseEntity> phaseList) {
		this.phaseList = phaseList;
	}
	
	
	
}
