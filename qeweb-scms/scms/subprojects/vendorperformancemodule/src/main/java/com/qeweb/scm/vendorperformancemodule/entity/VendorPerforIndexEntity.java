package com.qeweb.scm.vendorperformancemodule.entity;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 指标设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_index")
public class VendorPerforIndexEntity  extends BaseEntity{
	
	private String code;			//指标编码
	
	private String indexName;		//指标名称
	
	private String describe;		//指标描述
	
	private Integer otvd;			//一票否决维度
	
	private Integer otvv;			//一票否决供应商
	
	private Integer indexType;		//类型
	
	private long dimensionsId;		//维度ID
	
	private Long indexId;			//父指标ID
	
	private Integer hierarchy;		//层级
	
	private Integer limitPoints;	//扣分上限 default:0没有上限
	
	private VendorPerforDimensionsEntity dimensionsEntity;
	
	private Long _parentId;			//父ID
	
	/** 指标的集合 */
	private List<VendorPerforIndexEntity> childIndexList;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Integer getOtvd() {
		return otvd;
	}
	public void setOtvd(Integer otvd) {
		this.otvd = otvd;
	}
	public Integer getOtvv() {
		return otvv;
	}
	public void setOtvv(Integer otvv) {
		this.otvv = otvv;
	}
	public Integer getIndexType() {
		return indexType;
	}
	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}
	public long getDimensionsId() {
		return dimensionsId;
	}
	public void setDimensionsId(long dimensionsId) {
		this.dimensionsId = dimensionsId;
	}
	public Long getIndexId() {
		return indexId;
	}
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}
	public Integer getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public Integer getLimitPoints() {
		return limitPoints;
	}
	public void setLimitPoints(Integer limitPoints) {
		this.limitPoints = limitPoints;
	}
	@ManyToOne
	@JoinColumn(name = "dimensionsId",insertable = false,updatable = false)
	public VendorPerforDimensionsEntity getDimensionsEntity() {
		return dimensionsEntity;
	}
	public void setDimensionsEntity(VendorPerforDimensionsEntity dimensionsEntity) {
		this.dimensionsEntity = dimensionsEntity;
	}

	@Transient
	public List<VendorPerforIndexEntity> getChildIndexList() {
		return childIndexList;
	}
	public void setChildIndexList(List<VendorPerforIndexEntity> childIndexList) {
		this.childIndexList = childIndexList;
	}
	@Transient
	public Long get_parentId() {
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
}
