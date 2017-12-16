package com.qeweb.scm.vendormodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 邀请注册邮箱记录 创建时间: 2015年7月1日10:57:53
 * 
 * @author lw 更新时间: 2015年7月2日17:22:08 更新人：lw
 */
@Entity
@Table(name = "QEWEB_VENDOR_INVITE_EMAIL")
public class VendorInviteEmailEntity extends BaseEntity {

	private String vendorName;
	private String vendorEmail;
	private String inviteName;
	private String remark;
	private Timestamp expiryDate;
	private Integer isCheck;
	private Integer isRegister;

	private Long orgId; // 采购商ID
	private OrganizationEntity buyer;
	private String expiryDate1;

	
	@Transient
	public String getExpiryDate1() {
		return expiryDate1;
	}

	public void setExpiryDate1(String expiryDate1) {
		this.expiryDate1 = expiryDate1;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public String getInviteName() {
		return inviteName;
	}

	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(Integer isRegister) {
		this.isRegister = isRegister;
	}

	@Column(name="org_id")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	

	@ManyToOne
	@JoinColumn(name = "org_id",insertable = false,updatable=false)
	public OrganizationEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}

}
