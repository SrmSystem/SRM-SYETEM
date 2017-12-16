package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商驳回记录
 * @author chao.gu
 *
 */
@Entity
@Table(name = "qeweb_purchase_plan_reject")
public class PurchaseOrderItemPlanRejectEntity extends BaseEntity {

	private Long purchaseOrderItemPlanId;//供货关系id
	private Long purchaseOrderItemId;//采购订单明细ID
	private String reason;//原因
	public Long getPurchaseOrderItemPlanId() {
		return purchaseOrderItemPlanId;
	}
	public void setPurchaseOrderItemPlanId(Long purchaseOrderItemPlanId) {
		this.purchaseOrderItemPlanId = purchaseOrderItemPlanId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}
	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}
	
}
