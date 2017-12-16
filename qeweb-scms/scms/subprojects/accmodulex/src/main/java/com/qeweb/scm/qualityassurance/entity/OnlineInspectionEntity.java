package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;

/**
 *在线稽查不合格信息
 * @author sxl
 *
 */
@Entity
@Table(name = "qeweb_qm_onlineinspection")
public class OnlineInspectionEntity  extends BaseEntity{
	
	private Timestamp startTime;//开始时间
	
	private String code;
	
	private Long vendorId;//供应商ID
	
	private Long materialId;//物料ID
	
	private String describe;//表述

	private Integer totalNumber;//总数
	
	private Double money;//考核金额（元）
	
	private Integer stages;//发生地点有：1:产品审核、2:装配现场、3:客户反馈

	private Integer qualityStatus;//状态
	
	private Integer eightDStatus;//状态
	
	private VendorBaseInfoEntity vendorBaseInfoEntity;

	private MaterialEntity materialEntity;

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getStages() {
		return stages;
	}

	public void setStages(Integer stages) {
		this.stages = stages;
	}

	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}
	
	@Column(name="eightD_status")
	public Integer getEightDStatus() {
		return eightDStatus;
	}

	public void setEightDStatus(Integer eightDStatus) {
		this.eightDStatus = eightDStatus;
	}

	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}

	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}
	@ManyToOne
	@JoinColumn(name = "materialId",insertable = false,updatable = false)
	public MaterialEntity getMaterialEntity() {
		return materialEntity;
	}

	public void setMaterialEntity(MaterialEntity materialEntity) {
		this.materialEntity = materialEntity;
	}
}
