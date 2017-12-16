package com.qeweb.scm.vendorperformancemodule.entity;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 评估列表设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_scores")
public class VendorPerforScoresEntity  extends IdEntity{
	
	private Integer vendorNumber;//供应商数量	
	private Integer process;	//阶段
	private Timestamp assessStartDate;//评估开始日期
	private Timestamp assessEndDate;//评估结束日期
	private Long cycleId;//周期ID
	private Long perforTemplateId;//评估模版
	private String logPath;//日志路径，主要记录每次计算时的日志
	private Integer countStatus;//计算状态
	private Integer publishStatus;//发布状态
	private Long modelId;//绩效模型状态
	private String modelName;//绩效模型名称
	
	private VendorPerforTemplateEntity perforTemplate;//评估模版
	private VendorPerforCycleEntity cycleEntity;
	
	//非数据库字段
	private Timestamp countStartDate;//计算开始日期
	private Timestamp countEndDate;//计算结束日期
	private int isOutData;//是否来源于外部数据
	
	public Integer getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public Integer getProcess() {
		return process;
	}
	public void setProcess(Integer process) {
		this.process = process;
	}
	public Long getPerforTemplateId() {
		return perforTemplateId;
	}
	public void setPerforTemplateId(Long perforTemplateId) {
		this.perforTemplateId = perforTemplateId;
	}
	@ManyToOne
	@JoinColumn(name="perforTemplateId",insertable=false,updatable=false)	
	public VendorPerforTemplateEntity getPerforTemplate() {
		return perforTemplate;
	}
	public void setPerforTemplate(VendorPerforTemplateEntity perforTemplate) {
		this.perforTemplate = perforTemplate;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getAssessStartDate() {
		return assessStartDate;
	}
	public void setAssessStartDate(Timestamp assessStartDate) {
		this.assessStartDate = assessStartDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getAssessEndDate() {
		return assessEndDate;
	}
	public void setAssessEndDate(Timestamp assessEndDate) {
		this.assessEndDate = assessEndDate;
	}
	@ManyToOne
	@JoinColumn(name="cycleId",insertable=false,updatable=false)	
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}
	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public Integer getCountStatus() {
		return countStatus;
	}
	public void setCountStatus(Integer countStatus) {
		this.countStatus = countStatus;
	}
	public Integer getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	@Transient
	public Timestamp getCountStartDate() {
		return countStartDate;
	}
	public void setCountStartDate(Timestamp countStartDate) {
		this.countStartDate = countStartDate;
	}
	@Transient
	public Timestamp getCountEndDate() {
		return countEndDate;
	}
	public void setCountEndDate(Timestamp countEndDate) {
		this.countEndDate = countEndDate;
	}
	@Transient
	public int getIsOutData() {
		return isOutData;
	}
	public void setIsOutData(int isOutData) {
		this.isOutData = isOutData;
	}
}
