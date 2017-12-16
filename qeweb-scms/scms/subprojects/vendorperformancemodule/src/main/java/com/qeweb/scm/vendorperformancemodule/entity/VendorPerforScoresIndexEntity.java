package com.qeweb.scm.vendorperformancemodule.entity;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.IdEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;

/**
 * 指标得分
 * @author pjjxiajun
 * @date 2015年9月18日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresIndexEntity.java
 */
@Entity
@Table(name="qeweb_assess_scores_index")
public class VendorPerforScoresIndexEntity  extends IdEntity{
	
	private Long scoresId;
	private Long templateId;
	private Long templateSettingId;
	private Double score;
	private String name;
	private Long dimId;
	private Long indexId;
	private Double weight;
	private Long orgId;
	private Long materialTypeId;
	private Long brandId;
	private Long factoryId;
	private Long parentId;
	private Double reduceScore;//扣分
	private Timestamp performanceDate;//绩效日期
	
	private BussinessRangeEntity brand;
	private MaterialTypeEntity matType;
	private FactoryEntity factory;
	
	private String col1;
	private String col2;
	
	@Transient
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	@Transient
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public long getScoresId() {
		return scoresId;
	}
	public void setScoresId(long scoresId) {
		this.scoresId = scoresId;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDimId() {
		return dimId;
	}
	public void setDimId(Long dimId) {
		this.dimId = dimId;
	}
	public Long getIndexId() {
		return indexId;
	}
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public void setScoresId(Long scoresId) {
		this.scoresId = scoresId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getMaterialTypeId() {
		return materialTypeId;
	}
	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getTemplateSettingId() {
		return templateSettingId;
	}
	public void setTemplateSettingId(Long templateSettingId) {
		this.templateSettingId = templateSettingId;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="brandId",insertable=false,updatable=false)
	public BussinessRangeEntity getBrand() {
		return brand;
	}
	public void setBrand(BussinessRangeEntity brand) {
		this.brand = brand;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="materialTypeId",insertable=false,updatable=false)
	public MaterialTypeEntity getMatType() {
		return matType;
	}
	public void setMatType(MaterialTypeEntity matType) {
		this.matType = matType;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn(name="factoryId",insertable=false,updatable=false)
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	public Double getReduceScore() {
		return reduceScore;
	}
	public void setReduceScore(Double reduceScore) {
		this.reduceScore = reduceScore;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getPerformanceDate() {
		return performanceDate;
	}
	public void setPerformanceDate(Timestamp performanceDate) {
		this.performanceDate = performanceDate;
	}
	
	
	
	
	
}
