package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

@Entity
@Table(name = "QEWEB_8D_REPORT_DETAIL")
public class EightDReportDetail extends BaseEntity {
	
	private EightDRectification rectification;           // 主表
	private Integer reportType;                          //实例类型
	
	private MaterialEntity material;                     // 物料（向类拟零件展示）
	private String simIsProblem;                        // 是否存在问题（向类拟零件展示）
	private String simRemark;                            // 备注（向类拟零件展示）

	private String temConsiderMatter;                    // 考虑事项（临时围堵对策——立即的）
	private Double temQualifiedQty;                      // 合格数（临时围堵对策——立即的）
	private Double temUnqualifiedQty;                    // 不合格数（临时围堵对策——立即的）
	private String temHandleWay;                         // 不合格处置方式（临时围堵对策——立即的）

	private String reaCategory;                         // 类别（根本原因确认）
	private String reaIsProblem;                        // 有没有问题（根本原因确认）
	private String reaRootCause;                         // 问题产生的根本原因（根本原因确认）
	private String reaConfirmedWay;                      // 确认的方法、证据（根本原因确认）
	private Timestamp reaFinishTime;                          // 完成时间（根本原因确认）
	
	private String makNumber;                            // 序号（制定永久措施并实施）
	private String makRootCause;                         // 问题产生的根本原因（制定永久措施并实施）
	private String makEvidence;                          // 采取的永久措施及实施的证据（制定永久措施并实施）
	private String makDutyDept;                          // 责任部门（制定永久措施并实施）
	private Timestamp makFinishTime;                          // 完成时间（制定永久措施并实施）
	
	private String filName;                              // 须固化的文件（文件固化）
	private String filYes;                              // YES/NO（文件固化）
	private String filDept;                              // 实施部门（文件固化）
	private Timestamp filTime;                                // 实施时间（文件固化）
	private String filEvidence;                          // 实施的证据（（文件固化）
	
	// 非数据库字段
	private String materialCode;                          // 物料编码
	private String materialName;                          // 物料名称
	
	@ManyToOne
	@JoinColumn(name="rectification_id")
	public EightDRectification getRectification() {
		return rectification;
	}
	
	public void setRectification(EightDRectification rectification) {
		this.rectification = rectification;
	}
	
	@Column(name="report_type")
	public Integer getReportType() {
		return reportType;
	}
	
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	
	@ManyToOne
	@JoinColumn(name="sim_material_id")
	public MaterialEntity getMaterial() {
		return material;
	}
	
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
	@Column(name="sim_is_problem")
	public String getSimIsProblem() {
		return simIsProblem;
	}
	
	public void setSimIsProblem(String simIsProblem) {
		this.simIsProblem = simIsProblem;
	}
	
	@Column(name="sim_remark")
	public String getSimRemark() {
		return simRemark;
	}
	
	public void setSimRemark(String simRemark) {
		this.simRemark = simRemark;
	}
	
	@Column(name="tem_consider_matter")
	public String getTemConsiderMatter() {
		return temConsiderMatter;
	}
	
	public void setTemConsiderMatter(String temConsiderMatter) {
		this.temConsiderMatter = temConsiderMatter;
	}
	
	@Column(name="tem_qualified_qty")
	public Double getTemQualifiedQty() {
		return temQualifiedQty;
	}
	
	public void setTemQualifiedQty(Double temQualifiedQty) {
		this.temQualifiedQty = temQualifiedQty;
	}
	
	@Column(name="tem_unqualified_qty")
	public Double getTemUnqualifiedQty() {
		return temUnqualifiedQty;
	}
	
	public void setTemUnqualifiedQty(Double temUnqualifiedQty) {
		this.temUnqualifiedQty = temUnqualifiedQty;
	}
	
	@Column(name="tem_handle_way")
	public String getTemHandleWay() {
		return temHandleWay;
	}
	
	public void setTemHandleWay(String temHandleWay) {
		this.temHandleWay = temHandleWay;
	}
	
	@Column(name="rea_category")
	public String getReaCategory() {
		return reaCategory;
	}
	
	public void setReaCategory(String reaCategory) {
		this.reaCategory = reaCategory;
	}
	
	@Column(name="rea_is_problem")
	public String getReaIsProblem() {
		return reaIsProblem;
	}
	
	public void setReaIsProblem(String reaIsProblem) {
		this.reaIsProblem = reaIsProblem;
	}
	
	@Column(name="rea_root_cause")
	public String getReaRootCause() {
		return reaRootCause;
	}
	
	public void setReaRootCause(String reaRootCause) {
		this.reaRootCause = reaRootCause;
	}
	
	@Column(name="rea_confirmed_way")
	public String getReaConfirmedWay() {
		return reaConfirmedWay;
	}
	
	public void setReaConfirmedWay(String reaConfirmedWay) {
		this.reaConfirmedWay = reaConfirmedWay;
	}
	
	@Column(name="rea_finish_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getReaFinishTime() {
		return reaFinishTime;
	}
	
	public void setReaFinishTime(Timestamp reaFinishTime) {
		this.reaFinishTime = reaFinishTime;
	}
	
	@Column(name="mak_number")
	public String getMakNumber() {
		return makNumber;
	}
	
	public void setMakNumber(String makNumber) {
		this.makNumber = makNumber;
	}
	
	@Column(name="mak_root_cause")
	public String getMakRootCause() {
		return makRootCause;
	}
	
	public void setMakRootCause(String makRootCause) {
		this.makRootCause = makRootCause;
	}
	
	@Column(name="mak_evidence")
	public String getMakEvidence() {
		return makEvidence;
	}
	
	public void setMakEvidence(String makEvidence) {
		this.makEvidence = makEvidence;
	}
	
	@Column(name="mak_duty_dept")
	public String getMakDutyDept() {
		return makDutyDept;
	}
	
	public void setMakDutyDept(String makDutyDept) {
		this.makDutyDept = makDutyDept;
	}
	
	@Column(name="mak_finish_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getMakFinishTime() {
		return makFinishTime;
	}
	
	public void setMakFinishTime(Timestamp makFinishTime) {
		this.makFinishTime = makFinishTime;
	}
	
	@Column(name="fil_name")
	public String getFilName() {
		return filName;
	}
	
	public void setFilName(String filName) {
		this.filName = filName;
	}
	
	@Column(name="fil_yes")
	public String getFilYes() {
		return filYes;
	}
	
	public void setFilYes(String filYes) {
		this.filYes = filYes;
	}
	
	@Column(name="fil_dept")
	public String getFilDept() {
		return filDept;
	}
	
	public void setFilDept(String filDept) {
		this.filDept = filDept;
	}
	
	@Column(name="fil_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getFilTime() {
		return filTime;
	}
	
	public void setFilTime(Timestamp filTime) {
		this.filTime = filTime;
	}
	
	@Column(name="fil_evidence")
	public String getFilEvidence() {
		return filEvidence;
	}
	
	public void setFilEvidence(String filEvidence) {
		this.filEvidence = filEvidence;
	}

	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@Transient
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
}
