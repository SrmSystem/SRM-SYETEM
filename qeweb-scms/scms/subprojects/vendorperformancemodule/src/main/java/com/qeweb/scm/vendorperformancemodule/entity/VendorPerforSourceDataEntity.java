package com.qeweb.scm.vendorperformancemodule.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;

@Entity
@Table(name="QEWEB_ASSESS_SOURCEDATA")
public class VendorPerforSourceDataEntity extends BaseEntity{

	private String code;
	private Long performanceModelId;
	private String performanceModelName;
	private Long orgId;
	private String orgCode;
	private String orgName;
	private Long brandId;
	private String brandCode;
	private String brandName;
	private Long factoryId;
	private String factoryCode;
	private String factoryName;
	private Long matTypeId;
	private String matTypeCode;
	private String matTypeName;
	private Long dimId;
	private String dimCode;
	private String dimName;
	private Long indexId;
	private String indexCode;
	private String indexName;
	private String keyName;
	private String keyValue;
	private String describe;
	private Double score;
	private String attFileName;
	private String attFilePath;
	private Date assessTime;
	
	private BussinessRangeEntity bussinessRange;
	
	private String brandIds;
	
	public Long getPerformanceModelId() {
		return performanceModelId;
	}
	public void setPerformanceModelId(Long performanceModelId) {
		this.performanceModelId = performanceModelId;
	}
	public String getPerformanceModelName() {
		return performanceModelName;
	}
	public void setPerformanceModelName(String performanceModelName) {
		this.performanceModelName = performanceModelName;
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
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public Long getMatTypeId() {
		return matTypeId;
	}
	public void setMatTypeId(Long matTypeId) {
		this.matTypeId = matTypeId;
	}
	public String getMatTypeCode() {
		return matTypeCode;
	}
	public void setMatTypeCode(String matTypeCode) {
		this.matTypeCode = matTypeCode;
	}
	public String getMatTypeName() {
		return matTypeName;
	}
	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}
	public Long getDimId() {
		return dimId;
	}
	public void setDimId(Long dimId) {
		this.dimId = dimId;
	}
	public String getDimCode() {
		return dimCode;
	}
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public Long getIndexId() {
		return indexId;
	}
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}
	public String getIndexCode() {
		return indexCode;
	}
	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getAttFileName() {
		return attFileName;
	}
	public void setAttFileName(String attFileName) {
		this.attFileName = attFileName;
	}
	public String getAttFilePath() {
		return attFilePath;
	}
	public void setAttFilePath(String attFilePath) {
		this.attFilePath = attFilePath;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Date getAssessTime() {
		return assessTime;
	}
	public void setAssessTime(Date assessTime) {
		this.assessTime = assessTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne
	@JoinColumn(name="brandId",insertable=false,updatable=false)	
	public BussinessRangeEntity getBussinessRange() {
		return bussinessRange;
	}

	public void setBussinessRange(BussinessRangeEntity bussinessRange) {
		this.bussinessRange = bussinessRange;
	}
	
	@Transient
	public String getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}
	
	
	
}
