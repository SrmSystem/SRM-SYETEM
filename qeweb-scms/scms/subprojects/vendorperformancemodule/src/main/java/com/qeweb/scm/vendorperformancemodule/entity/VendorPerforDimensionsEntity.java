package com.qeweb.scm.vendorperformancemodule.entity;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 维度设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_dimensions")
public class VendorPerforDimensionsEntity  extends BaseEntity{
	
	private String code;
	
	private String dimName;	
	
	private String remarks;
	
	private Long parentId;
	
	private Integer factoryType;
	
	private String mappingScore;//映射的结果项，从score1-score20
	
	private Long _parentId;
	/* 非数据库集合 */
	/** 维度集合 */
	List<VendorPerforDimensionsEntity> children;
	/** 指标集合 */
	List<VendorPerforIndexEntity> childIndexList;
	
	
	private String state;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Transient
	public Long get_parentId() {
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	@Transient
	public List<VendorPerforIndexEntity> getChildIndexList() {
		return childIndexList;
	}
	public void setChildIndexList(List<VendorPerforIndexEntity> childIndexList) {
		this.childIndexList = childIndexList;
	}
	public String getMappingScore() {
		return mappingScore;
	}
	public void setMappingScore(String mappingScore) {
		this.mappingScore = mappingScore;
	}
	@Transient
	public List<VendorPerforDimensionsEntity> getChildren() {
		return children;
	}
	public void setChildren(List<VendorPerforDimensionsEntity> children) {
		this.children = children;
	}
	@Transient
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getFactoryType() {
		return factoryType;
	}
	public void setFactoryType(Integer factoryType) {
		this.factoryType = factoryType;
	}
	
}
