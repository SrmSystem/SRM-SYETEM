package com.qeweb.scm.vendorperformancemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;

//import javax.persistence.Entity;
//import javax.persistence.Table;

/**
 * 数据准备
 * @author Administrator
 *
 */
@Entity
@Table(name="qeweb_assess_dataindetails")
public class VendorPerforDataInDetailsEntity extends IdEntity {
	
	private long cycleId;//周期ID
	
	private String cycleName;//周期名称
	
	private Timestamp assessDate;//评估时间
	
	private Timestamp belongDate;//属于哪周期的时间
	
	private String vendorCode;//供应商Code	
	
	private String vendorName;//供应商名称	

	private String brandName;//品牌名称
	
	private String factoryName;//工厂名称
	
	private String indexName;//指标名称
	
	private String materialCode;//物料类别COde
	
	private String materialName;//物料类别名称
	
	private String dimensionsName;//维度名称
	
	private Integer mustImportElements;//应该导入要素
	
	private Integer noImportElements;//未导入要素
	
	private String noImportElementsName;//未导入要素
	
	private VendorPerforCycleEntity cycleEntity;
	
	@ManyToOne
	@JoinColumn(name="cycleId",insertable=false,updatable=false)		
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}
	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getAssessDate() {
		return assessDate;
	}
	public void setAssessDate(Timestamp assessDate) {
		this.assessDate = assessDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getBelongDate() {
		return belongDate;
	}
	public void setBelongDate(Timestamp belongDate) {
		this.belongDate = belongDate;
	}
	public long getCycleId() {
		return cycleId;
	}
	public void setCycleId(long cycleId) {
		this.cycleId = cycleId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getDimensionsName() {
		return dimensionsName;
	}
	public void setDimensionsName(String dimensionsName) {
		this.dimensionsName = dimensionsName;
	}
	public Integer getMustImportElements() {
		return mustImportElements;
	}
	public void setMustImportElements(Integer mustImportElements) {
		this.mustImportElements = mustImportElements;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	public Integer getNoImportElements() {
		return noImportElements;
	}
	public void setNoImportElements(Integer noImportElements) {
		this.noImportElements = noImportElements;
	}
	public String getNoImportElementsName() {
		return noImportElementsName;
	}
	public void setNoImportElementsName(String noImportElementsName) {
		this.noImportElementsName = noImportElementsName;
	}
}
