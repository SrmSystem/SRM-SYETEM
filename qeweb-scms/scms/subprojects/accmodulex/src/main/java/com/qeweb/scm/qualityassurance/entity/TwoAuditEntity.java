package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;


/**
 * 二方审核管理
 * @author SXL
 *
 */
@Entity
@Table(name = "qeweb_qm_twoaudit")
public class TwoAuditEntity extends BaseEntity{
	
	private Long orgId;	//供应商代码
	
	private String workOrder;	//工单
	
	private Timestamp correctionDate;//提出整改要求时间,添加数据必填
	
	private Timestamp requireDate;//要求整改完成时间,添加数据必填
	
	/** 整改状态 **/
	private Integer correctionStatus;//添加数据---默认0
	
	/** 结案状态 **/
	private Integer endStatus;//添加数据---默认0
	
	private String correctionContent;//审核计划内容,添加数据必填
	
	private String planFilePath;	//审核计划内容文件路径
	
	private String correctionEndContent;//审核报告结案评论
	
	private String reportFilePath;	//审核报告文件路径
	
	private OrganizationEntity org;	
	
	private String solutionContent;//整改方案
	
	private String fileUrl;//附件地址	

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public Timestamp getCorrectionDate() {
		return correctionDate;
	}

	public void setCorrectionDate(Timestamp correctionDate) {
		this.correctionDate = correctionDate;
	}

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

	public String getPlanFilePath() {
		return planFilePath;
	}

	public void setPlanFilePath(String planFilePath) {
		this.planFilePath = planFilePath;
	}

	public String getCorrectionEndContent() {
		return correctionEndContent;
	}

	public void setCorrectionEndContent(String correctionEndContent) {
		this.correctionEndContent = correctionEndContent;
	}

	public String getReportFilePath() {
		return reportFilePath;
	}

	public void setReportFilePath(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}

	@ManyToOne
	@JoinColumn(name="orgId",insertable=false,updatable=false)
	public OrganizationEntity getOrg() {
		return org;
	}

	public void setOrg(OrganizationEntity org) {
		this.org = org;
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
}
