package com.qeweb.scm.qualityassurance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "QEWEB_8D_REPORT")
public class EightDReport extends BaseEntity {
	
	private EightDRectification rectification;           // 主表
	private String repHappenPlace;				             // 发生地
	private Double repMalfunctionQty;			             // 故障件数量 
	private Double affectedBatchQty;			         // 受影响的批次数量 
	private String reportDescription;					 // 问题描述
	private String reason;					             // 原因分析
	private String measureVerification;					 // 永久措施效果验证
	private String specialMark;				         	 // 首批交付合格部品如何特殊标识/标记
	
	@ManyToOne
	@JoinColumn(name="rectification_id")
	public EightDRectification getRectification() {
		return rectification;
	}
	
	public void setRectification(EightDRectification rectification) {
		this.rectification = rectification;
	}
	
	@Column(name="happen_place")
	public String getRepHappenPlace() {
		return repHappenPlace;
	}
	
	public void setRepHappenPlace(String repHappenPlace) {
		this.repHappenPlace = repHappenPlace;
	}
	
	@Column(name="malfunction_qty")
	public Double getRepMalfunctionQty() {
		return repMalfunctionQty;
	}
	
	public void setRepMalfunctionQty(Double repMalfunctionQty) {
		this.repMalfunctionQty = repMalfunctionQty;
	}
	
	@Column(name="affectedBatch_qty")
	public Double getAffectedBatchQty() {
		return affectedBatchQty;
	}
	
	public void setAffectedBatchQty(Double affectedBatchQty) {
		this.affectedBatchQty = affectedBatchQty;
	}
	
	@Column(name="report_description")
	public String getReportDescription() {
		return reportDescription;
	}
	
	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}
	
	@Column(name="reason")
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name="measure_verification")
	public String getMeasureVerification() {
		return measureVerification;
	}
	
	public void setMeasureVerification(String measureVerification) {
		this.measureVerification = measureVerification;
	}
	
	@Column(name="special_mark")
	public String getSpecialMark() {
		return specialMark;
	}
	
	public void setSpecialMark(String specialMark) {
		this.specialMark = specialMark;
	}
	

}
