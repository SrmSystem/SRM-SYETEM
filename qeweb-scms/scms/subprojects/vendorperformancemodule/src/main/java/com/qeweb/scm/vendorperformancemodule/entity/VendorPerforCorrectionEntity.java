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
 * 供应商整改管理
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_correction")
public class VendorPerforCorrectionEntity  extends BaseEntity{
	
	private String vendorCode;//供应商Code,添加数据必填	
	
	private String vendorName;//供应商名称	,添加数据必填
	
	private Long brandId;
	
	private String brandName;//品牌名称
	
	private Long cycleId;//周期ID,添加数据必填
	
	private Timestamp assessDate;//评估时间,添加数据必填
	
	private Timestamp correctionDate;//提出整改要求时间,添加数据必填
	
	private Timestamp requireDate;//要求整改完成时间,添加数据必填
	
	/** 整改状态 **/
	private Integer correctionStatus;//添加数据---默认0
	
	/** 结案状态 **/
	private Integer endStatus;//添加数据---默认0
	
	private String correctionContent;//整改要求,添加数据必填

	private String planFilePath;	//整改要求文件路径
	
	private String correctionEndContent;//整改结案评论	
	
	private VendorPerforCycleEntity cycleEntity;
	
	/***透明字段***/
	private String solutionContent;
	
	/***透明字段***/
	private String fileUrl;//附件地址
	
	/***透明字段***/
	private String auditReason;//审核原因
	
	/***透明字段***/
	private String message;//审核原因
	
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
	
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getAssessDate() {
		return assessDate;
	}
	public void setAssessDate(Timestamp assessDate) {
		this.assessDate = assessDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCorrectionDate() {
		return correctionDate;
	}
	public void setCorrectionDate(Timestamp correctionDate) {
		this.correctionDate = correctionDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getRequireDate() {
		return requireDate;
	}
	public void setRequireDate(Timestamp requireDate) {
		this.requireDate = requireDate;
	}
	public Integer getCorrectionStatus() {
		return correctionStatus;
	}
	public void setCorrectionStatus(Integer correctionStatus) {
		this.correctionStatus = correctionStatus;
	}
	public Integer getEndStatus() {
		return endStatus;
	}
	public void setEndStatus(Integer endStatus) {
		this.endStatus = endStatus;
	}
	public String getCorrectionContent() {
		return correctionContent;
	}
	public void setCorrectionContent(String correctionContent) {
		this.correctionContent = correctionContent;
	}
	public String getCorrectionEndContent() {
		return correctionEndContent;
	}
	public void setCorrectionEndContent(String correctionEndContent) {
		this.correctionEndContent = correctionEndContent;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
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
	public String getSolutionContent() {
		return solutionContent;
	}
	public void setSolutionContent(String solutionContent) {
		this.solutionContent = solutionContent;
	}
	@Transient
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	@Transient
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	@Transient
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPlanFilePath() {
		return planFilePath;
	}
	public void setPlanFilePath(String planFilePath) {
		this.planFilePath = planFilePath;
	}
}
