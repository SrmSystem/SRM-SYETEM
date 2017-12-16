package com.qeweb.scm.vendorperformancemodule.entity;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;

/**
 * 总得分值对象
 * @author pjjxiajun
 * @date 2015年9月20日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity.java
 */
@Entity
@Table(name="qeweb_assess_scores_total")
public class VendorPerforScoresTotalEntity  extends BaseEntity{
	
	private Long scoresId;
	private Long modelId;
	private Long templateId;
	private Double totalScore;
	private Long orgId;
	private String orgCode;
	private String orgName;
	private Long materialTypeId;
	private String materialTypeName;
	private String materialTypeCode;
	private Long brandId;
	private String brandName;
	private String brandCode;
	private Long factoryId;
	private String factoryName;
	private String factoryCode;
	private Long parentId;
	private String cycle;
	private Long cycleId;
	private Timestamp assessDate;
	private Integer ranking;
	private String levelName;
	private String adjustReason;		//调分原因
	private Integer adjustStatus;		//调分状态，0-未调分，1已调分
	private Integer confirmStatus;		//确认状态 0-未确认 1-已确认
	private Integer publishStatus;		//发布状态
	private Integer correctionStatus;	//整改状态  0-未提交整改 1：已提交整改
	private Double reduceScore;//扣分
	private Timestamp performanceDate;//绩效时间
	private Integer year;//绩效的年份
	/** 被维度得分映射 */
	private Double score1;			//质量（麦特达因）
	private Double score2;			//RFQ（麦特达因）
	private Double score3;			//交付（麦特达因）
	private Double score4;
	private Double score5;
	private Double score6;			//8D（麦特达因）
	private Double score7;
	private Double score8;			//价格（麦特达因）
	private Double score9;
	private Double score10;
	private Double score11;
	private Double score12;
	private Double score13;
	private Double score14;
	private Double score15;
	private Double score16;
	private Double score17;
	private Double score18;
	private Double score19;
	private Double score20;
	//映射子绩效模版
	private Double totals1;
	private Double totals2;
	private Double totals3;
	private Double totals4;
	private Double totals5;
	private VendorPerforCycleEntity cycleEntity;
	
	/**非持久化字段
	 * 要求整改时间
	 */
	private Timestamp requireDate;
	/**
	 * 整改要求
	 */
	private String correctionContent;
	
	private BussinessRangeEntity brand;
	
	public Long getScoresId() {
		return scoresId;
	}
	public void setScoresId(Long scoresId) {
		this.scoresId = scoresId;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	@ManyToOne
	@JoinColumn(name = "cycleId",insertable = false,updatable = false)
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}
	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getAssessDate() {
		return assessDate;
	}
	public void setAssessDate(Timestamp assessDate) {
		this.assessDate = assessDate;
	}
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getAdjustReason() {
		return adjustReason;
	}
	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}
	public Integer getAdjustStatus() {
		return adjustStatus;
	}
	public void setAdjustStatus(Integer adjustStatus) {
		this.adjustStatus = adjustStatus;
	}
	public Integer getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public Integer getCorrectionStatus() {
		return correctionStatus;
	}
	public void setCorrectionStatus(Integer correctionStatus) {
		this.correctionStatus = correctionStatus;
	}
	public String getMaterialTypeName() {
		return materialTypeName;
	}
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	public String getMaterialTypeCode() {
		return materialTypeCode;
	}
	public void setMaterialTypeCode(String materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
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
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	@Transient
	public Double getReduceScore() {
		return reduceScore;
	}
	public void setReduceScore(Double reduceScore) {
		this.reduceScore = reduceScore;
	}
	public Double getScore1() {
		return score1;
	}
	public void setScore1(Double score1) {
		this.score1 = score1;
	}
	public Double getScore2() {
		return score2;
	}
	public void setScore2(Double score2) {
		this.score2 = score2;
	}
	public Double getScore3() {
		return score3;
	}
	public void setScore3(Double score3) {
		this.score3 = score3;
	}
	public Double getScore4() {
		return score4;
	}
	public void setScore4(Double score4) {
		this.score4 = score4;
	}
	public Double getScore5() {
		return score5;
	}
	public void setScore5(Double score5) {
		this.score5 = score5;
	}
	public Double getScore6() {
		return score6;
	}
	public void setScore6(Double score6) {
		this.score6 = score6;
	}
	public Double getScore7() {
		return score7;
	}
	public void setScore7(Double score7) {
		this.score7 = score7;
	}
	public Double getScore8() {
		return score8;
	}
	public void setScore8(Double score8) {
		this.score8 = score8;
	}
	public Double getScore9() {
		return score9;
	}
	public void setScore9(Double score9) {
		this.score9 = score9;
	}
	public Double getScore10() {
		return score10;
	}
	public void setScore10(Double score10) {
		this.score10 = score10;
	}
	public Double getScore11() {
		return score11;
	}
	public void setScore11(Double score11) {
		this.score11 = score11;
	}
	public Double getScore12() {
		return score12;
	}
	public void setScore12(Double score12) {
		this.score12 = score12;
	}
	public Double getScore13() {
		return score13;
	}
	public void setScore13(Double score13) {
		this.score13 = score13;
	}
	public Double getScore14() {
		return score14;
	}
	public void setScore14(Double score14) {
		this.score14 = score14;
	}
	public Double getScore15() {
		return score15;
	}
	public void setScore15(Double score15) {
		this.score15 = score15;
	}
	public Double getScore16() {
		return score16;
	}
	public void setScore16(Double score16) {
		this.score16 = score16;
	}
	public Double getScore17() {
		return score17;
	}
	public void setScore17(Double score17) {
		this.score17 = score17;
	}
	public Double getScore18() {
		return score18;
	}
	public void setScore18(Double score18) {
		this.score18 = score18;
	}
	public Double getScore19() {
		return score19;
	}
	public void setScore19(Double score19) {
		this.score19 = score19;
	}
	public Double getScore20() {
		return score20;
	}
	public void setScore20(Double score20) {
		this.score20 = score20;
	}
	@Transient
	public Timestamp getRequireDate() {
		return requireDate;
	}
	public void setRequireDate(Timestamp requireDate) {
		this.requireDate = requireDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getPerformanceDate() {
		return performanceDate;
	}
	public void setPerformanceDate(Timestamp performanceDate) {
		this.performanceDate = performanceDate;
	}
	
	public Double getTotals1() {
		return totals1;
	}
	public void setTotals1(Double totals1) {
		this.totals1 = totals1;
	}
	public Double getTotals2() {
		return totals2;
	}
	public void setTotals2(Double totals2) {
		this.totals2 = totals2;
	}
	public Double getTotals3() {
		return totals3;
	}
	public void setTotals3(Double totals3) {
		this.totals3 = totals3;
	}
	public Double getTotals4() {
		return totals4;
	}
	public void setTotals4(Double totals4) {
		this.totals4 = totals4;
	}
	public Double getTotals5() {
		return totals5;
	}
	public void setTotals5(Double totals5) {
		this.totals5 = totals5;
	}
	@Transient
	public String getCorrectionContent() {
		return correctionContent;
	}
	public void setCorrectionContent(String correctionContent) {
		this.correctionContent = correctionContent;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	@ManyToOne(optional=true)
	@JoinColumn(name="brandId",insertable=false,updatable=false)
	public BussinessRangeEntity getBrand() {
		return brand;
	}
	
	public void setBrand(BussinessRangeEntity brand) {
		this.brand = brand;
	}
	
}
