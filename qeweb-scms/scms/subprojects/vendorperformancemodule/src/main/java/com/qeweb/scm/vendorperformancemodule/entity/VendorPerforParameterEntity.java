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
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 导入数据
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_parameter")
public class VendorPerforParameterEntity  extends BaseEntity{
	
	
	private Long orgId;	 //供应商ID
	
	private Long cycleId;//周期ID
	
	private Long factoryId;//工厂ID
	
	private Long brandId;//品牌
	
	private String brandName;//品牌名称
	
	private String parameter;//参数名称
	
	private String parameterValue;//参数值
	
	private Timestamp assessDate;//评估时间
	
	private Integer joinStatus;//参评状态
	
	private VendorPerforCycleEntity cycleEntity;
	
	private FactoryEntity factoryEntity;

	private OrganizationEntity org;
	
	private BussinessRangeEntity bussinessRange;
	
	private String orgName;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getAssessDate() {
		return assessDate;
	}

	public void setAssessDate(Timestamp assessDate) {
		this.assessDate = assessDate;
	}

	public Integer getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(Integer joinStatus) {
		this.joinStatus = joinStatus;
	}
	

	

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	@ManyToOne
	@JoinColumn(name="cycleId",insertable=false,updatable=false)	
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}

	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}

	@ManyToOne
	@JoinColumn(name="factoryId",insertable=false,updatable=false)	
	public FactoryEntity getFactoryEntity() {
		return factoryEntity;
	}

	public void setFactoryEntity(FactoryEntity factoryEntity) {
		this.factoryEntity = factoryEntity;
	}

	@ManyToOne
	@JoinColumn(name="orgId",insertable=false,updatable=false)	
	public OrganizationEntity getOrg() {
		return org;
	}

	public void setOrg(OrganizationEntity org) {
		this.org = org;
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
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
