package com.qeweb.scm.basemodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "QEWEB_MATERIAL_TYPE")
public class MaterialTypeEntity extends BaseEntity {

	private String code;
	private String name;
	private Long parentId;
	private Long parentTopId;
	private Integer levelLayer;//级别层次
	private String levelInfo;
	private String levelDescribe;
	private Integer leaf;
	private Integer importance;//重要程度
	private Integer needSecondVendor;//是否为二级供应商
	private String remark;
	
	private String col1;//扩展字段1,科室
	
	private String col2;//扩展字段2,类型 1：采购员使用的
	
	private Long _parentId;
	
	/* 非数据库集合 */
	/** 维度集合 */
	List<MaterialTypeEntity> children;
	/** 指标集合 */
	List<MaterialTypeEntity> childIndexList;
	
	private String state;

	private String faname;
	
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
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
		if(parentId==null){
			this.parentId = 0l;
		}
	}
	public Long getParentTopId() {
		return parentTopId;
	}
	public void setParentTopId(Long parentTopId) {
		this.parentTopId = parentTopId;
		if(this.parentTopId==null) {
			this.parentTopId = 0l;
		}
	}
	public String getLevelInfo() {
		return levelInfo;
	}
	public void setLevelInfo(String levelInfo) {
		this.levelInfo = levelInfo;
	}
	public String getLevelDescribe() {
		return levelDescribe;
	}
	public void setLevelDescribe(String levelDescribe) {
		this.levelDescribe = levelDescribe;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getLeaf() {
		return leaf;
	}
	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}
	public Integer getImportance() {
		return importance;
	}
	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	public Integer getNeedSecondVendor() {
		return needSecondVendor;
	}
	public void setNeedSecondVendor(Integer needSecondVendor) {
		this.needSecondVendor = needSecondVendor;
	}
	public Integer getLevelLayer() {
		return levelLayer;
	}
	public void setLevelLayer(Integer levelLayer) {
		this.levelLayer = levelLayer;
	}
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	@Transient
	public String getFaname() {
		return faname;
	}
	public void setFaname(String faname) {
		this.faname = faname;
	}
	@Transient
	public Long get_parentId() {
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	@Transient
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Transient
	public List<MaterialTypeEntity> getChildren() {
		return children;
	}
	public void setChildren(List<MaterialTypeEntity> children) {
		this.children = children;
	}
	@Transient
	public List<MaterialTypeEntity> getChildIndexList() {
		return childIndexList;
	}
	public void setChildIndexList(List<MaterialTypeEntity> childIndexList) {
		this.childIndexList = childIndexList;
	}
	
}
