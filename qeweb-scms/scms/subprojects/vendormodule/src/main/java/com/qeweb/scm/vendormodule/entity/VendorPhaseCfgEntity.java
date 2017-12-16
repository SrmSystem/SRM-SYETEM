package com.qeweb.scm.vendormodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 供应商阶段的配置关系，每个供应商都有和阶段的配置关系，这个关系初始是由选择
 * 或指定的导航模版决定的
 * @author pjjxiajun
 * @date 2015年5月24日
 * @path com.qeweb.scm.vendormodule.entity.VendorPhaseCfgEntity.java
 */
@Entity
@Table(name="qeweb_vendor_phase_cfg")
public class VendorPhaseCfgEntity extends IdEntity{
	
	private Long phaseId;
	private String phaseCode;
	private String phaseName;
	private Integer phaseSn;
	private Long vendorId;
	private Long orgId;
	private Long templateId;
	private VendorPhaseEntity vendorPhase;
	
	//非数据库使用
	private List<VendorSurveyCfgEntity> vendorSurveyCfgList;
	
	public Long getPhaseId() {
		return phaseId;
	}
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	public String getPhaseCode() {
		return phaseCode;
	}
	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}
	public String getPhaseName() {
		return phaseName;
	}
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public Integer getPhaseSn() {
		return phaseSn;
	}
	public void setPhaseSn(Integer phaseSn) {
		this.phaseSn = phaseSn;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	@ManyToOne
	@JoinColumn(name="phaseId",insertable=false,updatable=false)
	public VendorPhaseEntity getVendorPhase() {
		return vendorPhase;
	}
	public void setVendorPhase(VendorPhaseEntity vendorPhase) {
		this.vendorPhase = vendorPhase;
	}
	@Transient
	public List<VendorSurveyCfgEntity> getVendorSurveyCfgList() {
		return vendorSurveyCfgList;
	}
	public void setVendorSurveyCfgList(List<VendorSurveyCfgEntity> vendorSurveyCfgList) {
		this.vendorSurveyCfgList = vendorSurveyCfgList;
	}
	
	
	
	
	
}