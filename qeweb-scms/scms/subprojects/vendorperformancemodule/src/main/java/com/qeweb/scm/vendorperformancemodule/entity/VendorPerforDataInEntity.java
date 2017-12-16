package com.qeweb.scm.vendorperformancemodule.entity;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 导入数据
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_datain")
public class VendorPerforDataInEntity  extends BaseEntity{
	private Long modelId;
	private long cycleId;//周期ID
	
	private String indexName;//指标名称
	
	private Long orgId;
	private String vendorCode;//供应商Code	
	
	private String vendorName;//供应商名称	
	
	private Long brandId;
	private String brandName;//品牌名称			//麦特达因专用，把不是周的次数(百分比)数据,放入brandName中
	
	private Long matTypeId;
	private String materialCode;//物料类别COde	
	
	private String materialName;//物料类别名称		//麦特达因专用，把体系认证名称,放入materialName中，用逗号隔开
	
	private Long factoryId;
	private String factoryName;//工厂名称			
	
	private String element;//要素名称
	
	private String elementValue;//要素值
	
	private Timestamp assessDate;//评估时间
	
	private Timestamp belongDate;//属于哪周期的时间
	
	private VendorPerforCycleEntity cycleEntity;
	
	//add by zhangjiejun 2015.12.02 start
	private double score;
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	//add by zhangjiejun 2015.12.02 end
	
	//透明字段
	private String assessDateStart;
	//透明字段
	private String assessDateEnd;
	
	//非持久化字段
	
	private String deliveryCodes;		//发货单号，逗号隔开
	
	private String times;				//时间，逗号隔开
	
	@Transient
	public String getDeliveryCodes() {
		return deliveryCodes;
	}
	public void setDeliveryCodes(String deliveryCodes) {
		this.deliveryCodes = deliveryCodes;
	}
	
	@Transient
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	
	public long getCycleId() {
		return cycleId;
	}
	public void setCycleId(long cycleId) {
		this.cycleId = cycleId;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getElementValue() {
		return elementValue;
	}
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	@ManyToOne
	@JoinColumn(name="cycleId",insertable=false,updatable=false)		
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}
	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	@Transient
	public String getAssessDateStart() {
		return assessDateStart;
	}
	public void setAssessDateStart(String assessDateStart) {
		this.assessDateStart = assessDateStart;
	}
	@Transient
	public String getAssessDateEnd() {
		return assessDateEnd;
	}
	public void setAssessDateEnd(String assessDateEnd) {
		this.assessDateEnd = assessDateEnd;
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
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getMatTypeId() {
		return matTypeId;
	}
	public void setMatTypeId(Long matTypeId) {
		this.matTypeId = matTypeId;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	
}
