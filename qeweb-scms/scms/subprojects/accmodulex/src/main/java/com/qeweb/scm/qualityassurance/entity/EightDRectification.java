package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

@Entity
@Table(name = "QEWEB_8D_RECTIFICATION")
public class EightDRectification extends BaseEntity {
	
	private OrganizationEntity vendor;         // 供应商
	private MaterialEntity material;		   // 物料
	private String happenPlace;				   // 发生地
	private Double malfunctionQty;			   // 故障件数量
	private String recDescription;			   // 现象描述
	private Integer status;					   // 状态
	private Integer publishStatus;			   // 发布状态
	private Integer reproveStatus;			   // 整改状态
	private String approveAdvice;			   // 审核意见
	private Timestamp happenTime;			   // 事故时间
	private String attachmentName;			   // 文件名
	private String attachmentPath;			   // 文件路径
	private String reproveAttachmentName;			   // 整改文件名
	private String reproveAttachmentPath;			   // 整改文件路径
	private EightDReport eightDReport;
	private Set<EightDReportDetail> details;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}
	
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	
	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}
	
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	@Column(name="happen_place")
	public String getHappenPlace() {
		return happenPlace;
	}
	
	public void setHappenPlace(String happenPlace) {
		this.happenPlace = happenPlace;
	}
	
	@Column(name="malfunction_qty")
	public Double getMalfunctionQty() {
		return malfunctionQty;
	}
	
	public void setMalfunctionQty(Double malfunctionQty) {
		this.malfunctionQty = malfunctionQty;
	}
	
	@Column(name="rec_description")
	public String getRecDescription() {
		return recDescription;
	}
	
	public void setRecDescription(String recDescription) {
		this.recDescription = recDescription;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name="publish_status")
	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	@Column(name="reprove_status")
	public Integer getReproveStatus() {
		return reproveStatus;
	}

	public void setReproveStatus(Integer reproveStatus) {
		this.reproveStatus = reproveStatus;
	}

	@Column(name="approve_advice")
	public String getApproveAdvice() {
		return approveAdvice;
	}
	
	public void setApproveAdvice(String approveAdvice) {
		this.approveAdvice = approveAdvice;
	}
	
	@Column(name="happen_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getHappenTime() {
		return happenTime;
	}
	
	public void setHappenTime(Timestamp happenTime) {
		this.happenTime = happenTime;
	}
	
	@Column(name="attachment_name")
	public String getAttachmentName() {
		return attachmentName;
	}
	
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	
	@Column(name="attachment_path")
	public String getAttachmentPath() {
		return attachmentPath;
	}
	
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	
	@Column(name="reprove_attachment_name")
	public String getReproveAttachmentName() {
		return reproveAttachmentName;
	}

	public void setReproveAttachmentName(String reproveAttachmentName) {
		this.reproveAttachmentName = reproveAttachmentName;
	}

	@Column(name="reprove_attachment_path")
	public String getReproveAttachmentPath() {
		return reproveAttachmentPath;
	}

	public void setReproveAttachmentPath(String reproveAttachmentPath) {
		this.reproveAttachmentPath = reproveAttachmentPath;
	}

	@ManyToOne
	@JoinColumn(name="eightD_report_id")
	public EightDReport getEightDReport() {
		return eightDReport;
	}
	
	public void setEightDReport(EightDReport eightDReport) {
		this.eightDReport = eightDReport;
	}
	
	@OneToMany(mappedBy="rectification")
	@JsonIgnore
	public Set<EightDReportDetail> getDetails() {
		return details;
	}
	
	public void setDetails(Set<EightDReportDetail> details) {
		this.details = details;
	}
	
	

}
