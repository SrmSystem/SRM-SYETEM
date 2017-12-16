package com.qeweb.scm.vendorperformancemodule.vo;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.utils.DateUtil;

/**
 * 总得分
 * @author pjjxiajun
 * @date 2015年9月20日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity.java
 */
public class VendorPerforScoresTotalVo{
	
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
	private String adjustReason;//调分原因
	private Integer adjustStatus;//调分状态，0-未调分，1已调分
	private Integer publishStatus;//发布状态
	private Double reduceScore;//扣分
	private Integer year;
	/** 被维度得分映射 */
	private Double score1;
	private Double score2;
	private Double score3;
	private Double score4;
	private Double score5;
	private Double score6;
	private Double score7;
	private Double score8;
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
	/** 被维度得分映射 */
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String level5;
	private String level6;
	private String level7;
	private String level8;
	private String level9;
	private String level10;
	private String level11;
	private String level12;
	private String level13;
	private String level14;
	private String level15;
	private String level16;
	private String level17;
	private String level18;
	private String level19;
	private String level20;
	
	
	
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
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
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
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
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
	public Double getReduceScore() {
		return reduceScore;
	}
	public void setReduceScore(Double reduceScore) {
		this.reduceScore = reduceScore;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getLevel4() {
		return level4;
	}
	public void setLevel4(String level4) {
		this.level4 = level4;
	}
	public String getLevel5() {
		return level5;
	}
	public void setLevel5(String level5) {
		this.level5 = level5;
	}
	public String getLevel6() {
		return level6;
	}
	public void setLevel6(String level6) {
		this.level6 = level6;
	}
	public String getLevel7() {
		return level7;
	}
	public void setLevel7(String level7) {
		this.level7 = level7;
	}
	public String getLevel8() {
		return level8;
	}
	public void setLevel8(String level8) {
		this.level8 = level8;
	}
	public String getLevel9() {
		return level9;
	}
	public void setLevel9(String level9) {
		this.level9 = level9;
	}
	public String getLevel10() {
		return level10;
	}
	public void setLevel10(String level10) {
		this.level10 = level10;
	}
	public String getLevel11() {
		return level11;
	}
	public void setLevel11(String level11) {
		this.level11 = level11;
	}
	public String getLevel12() {
		return level12;
	}
	public void setLevel12(String level12) {
		this.level12 = level12;
	}
	public String getLevel13() {
		return level13;
	}
	public void setLevel13(String level13) {
		this.level13 = level13;
	}
	public String getLevel14() {
		return level14;
	}
	public void setLevel14(String level14) {
		this.level14 = level14;
	}
	public String getLevel15() {
		return level15;
	}
	public void setLevel15(String level15) {
		this.level15 = level15;
	}
	public String getLevel16() {
		return level16;
	}
	public void setLevel16(String level16) {
		this.level16 = level16;
	}
	public String getLevel17() {
		return level17;
	}
	public void setLevel17(String level17) {
		this.level17 = level17;
	}
	public String getLevel18() {
		return level18;
	}
	public void setLevel18(String level18) {
		this.level18 = level18;
	}
	public String getLevel19() {
		return level19;
	}
	public void setLevel19(String level19) {
		this.level19 = level19;
	}
	public String getLevel20() {
		return level20;
	}
	public void setLevel20(String level20) {
		this.level20 = level20;
	}

	
	
	
	
}
