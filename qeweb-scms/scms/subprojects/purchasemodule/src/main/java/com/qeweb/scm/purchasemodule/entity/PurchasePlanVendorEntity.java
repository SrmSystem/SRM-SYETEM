package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
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
	private Set<PurchasePlanVenodrItemEntity> vendorPlanItem;

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

	@OneToMany(fetch=FetchType.LAZY, mappedBy="vendorPlan") 
	@Where(clause="abolished=0")
	@JsonIgnore
	public Set<PurchasePlanVenodrItemEntity> getVendorPlanItem() {
		return vendorPlanItem;
	}

	public void setVendorPlanItem(Set<PurchasePlanVenodrItemEntity> vendorPlanItem) {
		this.vendorPlanItem = vendorPlanItem;
	}

}
