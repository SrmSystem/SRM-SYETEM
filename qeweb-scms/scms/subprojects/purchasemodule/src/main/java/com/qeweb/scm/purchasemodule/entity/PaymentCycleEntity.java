package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 付款周期设置
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_payment_cycle")
public class PaymentCycleEntity extends BaseEntity {

	private OrganizationEntity vendor; 	// 供应商
	private OrganizationEntity buyer; 	// 采购商
	private Integer paymentCycle; 		// 1:30天付款 2：60天付款 3:90天付款

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name="buyer_id")
	public OrganizationEntity getBuyer() {
		return buyer;
	}

	public void setBuyer(OrganizationEntity buyer) {
		this.buyer = buyer;
	}

	@Column(name="payment_cycle")
	public Integer getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle) {
		this.paymentCycle = paymentCycle;
	}
	
}
