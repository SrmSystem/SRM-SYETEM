package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "qeweb_delivery_pc")
public class DeliveryPCEntity extends BaseEntity {

	private DeliveryItemEntity deliveryItem;
	private String asnCode;//条码号
	private Double boxCount;//数量
	private Integer printStatus;//打印状态
	
	@ManyToOne
	@JoinColumn(name="delivery_item_id")
	public DeliveryItemEntity getDeliveryItem() {
		return deliveryItem;
	}
	public void setDeliveryItem(DeliveryItemEntity deliveryItem) {
		this.deliveryItem = deliveryItem;
	}
	public String getAsnCode() {
		return asnCode;
	}
	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}
	public Double getBoxCount() {
		return boxCount;
	}
	public void setBoxCount(Double boxCount) {
		this.boxCount = boxCount;
	}
	public Integer getPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(Integer printStatus) {
		this.printStatus = printStatus;
	}
	
}
