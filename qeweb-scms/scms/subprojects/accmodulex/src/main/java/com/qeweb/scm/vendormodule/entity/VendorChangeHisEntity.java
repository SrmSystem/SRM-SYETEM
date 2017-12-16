package com.qeweb.scm.vendormodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 供应商变更历史表
 * @author pjjxiajun 
 * @date 2015年7月9日
 * @path com.qeweb.scm.vendormodule.entity.VendorChangeHisEntity.java
 */
@Entity
@Table(name="QEWEB_VENDOR_CHANGEHIS")
public class VendorChangeHisEntity extends IdEntity{
	
	private Long orgId;//组织ID
	private String vendorName;//供应商名称
	
	private Integer changeType;//变更类型
	private String changeTypeText;// 变更类型文本
	
	private String changeUser;//变更人
	private Timestamp changeTime;//变更时间
	private String changeReason;//变更原因
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	public String getChangeTypeText() {
		return changeTypeText;
	}
	public void setChangeTypeText(String changeTypeText) {
		this.changeTypeText = changeTypeText;
	}
	public String getChangeUser() {
		return changeUser;
	}
	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
