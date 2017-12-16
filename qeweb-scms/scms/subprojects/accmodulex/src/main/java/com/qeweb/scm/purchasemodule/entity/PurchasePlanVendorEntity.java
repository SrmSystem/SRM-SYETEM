package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

@Entity
@Table(name = "qeweb_purchase_plan_vendor")
public class PurchasePlanVendorEntity extends BaseEntity {

	private PurchasePlanEntity plan;
	private OrganizationEntity vendor;
	private Integer seeStatus;
	private Integer confirmStatus;
	private UserEntity confrimUser;
	private Timestamp confirmTime;
	
	//add By hao.qin
	private UserEntity  buyer;              //采购员
	private Integer uploadStatus;			// 上传产能表状态  0：未上传 1：部分上传 2：已上传
	private PurchasingGroupEntity group; //采购组
	private String month;//版本
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	@ManyToOne
	@JoinColumn(name="buyer_id")
	public UserEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(UserEntity buyer) {
		this.buyer = buyer;
	}
	

	public Integer getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
	
	@ManyToOne
	@JoinColumn(name="group_id")
	public PurchasingGroupEntity getGroup() {
		return group;
	}

	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	
	

	@ManyToOne
	@JoinColumn(name="plan_id")
	public PurchasePlanEntity getPlan() {
		return plan;
	}

	public void setPlan(PurchasePlanEntity plan) {
		this.plan = plan;
	}

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@Column(name="see_status")
	public Integer getSeeStatus() {
		return seeStatus;
	}

	public void setSeeStatus(Integer seeStatus) {
		this.seeStatus = seeStatus;
	}

	@Column(name="confirm_status")
	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	@ManyToOne
	@JoinColumn(name="confirm_user_id")
	public UserEntity getConfrimUser() {
		return confrimUser;
	}

	public void setConfrimUser(UserEntity confrimUser) {
		this.confrimUser = confrimUser;
	}

	@Column(name="confirm_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

}
