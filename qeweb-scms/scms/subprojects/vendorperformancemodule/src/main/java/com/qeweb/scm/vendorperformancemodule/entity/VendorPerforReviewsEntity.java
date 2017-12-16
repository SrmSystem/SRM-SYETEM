package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;

/**
 * 参评设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_reviews")
public class VendorPerforReviewsEntity  extends BaseEntity{
	
	private Long vendorId;
	
	private Long cycleId;
	
	private Long orgId;
	
	private String orgName;
	
	private String orgCode;
	/** 参评状态 */
	private Integer joinStatus;
	/** 参评绩效模型ID */
	private Long modelId;
	/** 参评绩效模型名称 */
	private String modelName;
	
	private VendorPerforCycleEntity cycleEntity;
	
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	//----------------------------非持久化 字段
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	@ManyToOne
	@JoinColumn(name = "cycleId",insertable = false,updatable = false)
	public VendorPerforCycleEntity getCycleEntity() {
		return cycleEntity;
	}

	public void setCycleEntity(VendorPerforCycleEntity cycleEntity) {
		this.cycleEntity = cycleEntity;
	}
	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}

	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}

	public Integer getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(Integer joinStatus) {
		this.joinStatus = joinStatus;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	public String getCol1() {
		return col1;
	}
	
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	@Transient
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	@Transient
	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}
	@Transient
	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}
	@Transient
	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}
	@Transient
	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}
	@Transient
	public String getCol7() {
		return col7;
	}

	public void setCol7(String col7) {
		this.col7 = col7;
	}
	@Transient
	public String getCol8() {
		return col8;
	}

	public void setCol8(String col8) {
		this.col8 = col8;
	}
}
