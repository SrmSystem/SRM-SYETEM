package com.qeweb.scm.vendormodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 邀请注册邮箱记录
 * 创建时间: 2015年7月1日10:57:53
 * @author lw
 * 更新时间: 2015年7月2日17:22:08
 * 更新人：lw
 */
@Entity
@Table(name = "QEWEB_VENDOR_INVITE_EMAIL")
public class VendorInviteEmailEntity extends BaseEntity{

	private String vendorName;
	private String vendorEmail;
	private String inviteName;
	private String remark;
	private Timestamp expiryDate;
	private Integer isCheck;
	private Integer isRegister;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8:00")
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

}
