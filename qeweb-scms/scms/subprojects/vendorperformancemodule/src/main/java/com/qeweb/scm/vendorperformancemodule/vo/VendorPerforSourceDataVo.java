package com.qeweb.scm.vendorperformancemodule.vo;

import java.util.Date;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.basemodule.entity.BaseEntity;

@ExcelTransfer(start=1, describe = "绩效基础数据导入") 
public class VendorPerforSourceDataVo extends BaseEntity{

	@ExcelCol(column = Column.A, required = true,desc="绩效模式")
	private String performanceModelName;
	
	@ExcelCol(column = Column.B, required = true,desc="供应商编码")
	private String orgCode;
	@ExcelCol(column = Column.C, required = true,desc="供应商名称")	
	private String orgName;
	@ExcelCol(column = Column.D, required = true,desc="品牌编码")
	private String brandCode;
	@ExcelCol(column = Column.E, required = true,desc="品牌")
	private String brandName;
	@ExcelCol(column = Column.F, required = true,desc="工厂")
	private String factoryName;
	@ExcelCol(column = Column.G, required = true,desc="分类编码")
	private String matTypeCode;
	@ExcelCol(column = Column.H, required = true,desc="分类")
	private String matTypeName;
	@ExcelCol(column = Column.I, required = true,desc="维度")
	private String dimName;
	@ExcelCol(column = Column.J, required = true,desc="指标")
	private String indexName;
	@ExcelCol(column = Column.K, required = true,desc="要素")	
	private String keyName;
	@ExcelCol(column = Column.L, type=ColType.NUMBER, required = true,desc="要素值")	
	private Double keyValue;
	@ExcelCol(column = Column.M,type=ColType.NUMBER, desc="得分")	
	private Double score;
	@ExcelCol(column = Column.N, required = true, desc="问题详细描述")	
	private String describe;
	@ExcelCol(column = Column.O,type=ColType.DATE, required = true,desc="评估时间")	
	private Date assessTime;
	public String getPerformanceModelName() {
		return performanceModelName;
	}
	public void setPerformanceModelName(String performanceModelName) {
		this.performanceModelName = performanceModelName;
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
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getMatTypeName() {
		return matTypeName;
	}
	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
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
	public Double getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(Double keyValue) {
		this.keyValue = keyValue;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Date getAssessTime() {
		return assessTime;
	}
	public void setAssessTime(Date assessTime) {
		this.assessTime = assessTime;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getMatTypeCode() {
		return matTypeCode;
	}
	public void setMatTypeCode(String matTypeCode) {
		this.matTypeCode = matTypeCode;
	}

	

	
	
	
}
