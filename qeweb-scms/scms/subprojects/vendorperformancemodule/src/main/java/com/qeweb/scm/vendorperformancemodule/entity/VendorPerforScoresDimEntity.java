package com.qeweb.scm.vendorperformancemodule.entity;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 维度得分
 * @author pjjxiajun
 * @date 2015年9月18日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresDimEntity.java
 */
@Entity
@Table(name="qeweb_assess_scores_dim")
public class VendorPerforScoresDimEntity  extends IdEntity{
	
	private Long scoresId;
	private Long templateId;
	private Long templateSettingId;
	private Double score;
	private String name;
	private Long dimId;
	private Long dimParentId;
	private Double weight;
	private Long orgId;
	private Long materialTypeId;
	private Long brandId;
	private Long factoryId;
	private Long parentId;
	private Double reduceScore;//扣分
	private Timestamp performanceDate;//绩效时间
	private Integer settingType;//设置类型
	

	
	
	public Long getScoresId() {
		return scoresId;
	}
	public Long getTemplateId() {
		return templateId;
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
	public Long getDimParentId() {
		return dimParentId;
	}
	public void setDimParentId(Long dimParentId) {
		this.dimParentId = dimParentId;
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
	public Integer getSettingType() {
		return settingType;
	}
	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}
	
	
	
	
	
	
}
