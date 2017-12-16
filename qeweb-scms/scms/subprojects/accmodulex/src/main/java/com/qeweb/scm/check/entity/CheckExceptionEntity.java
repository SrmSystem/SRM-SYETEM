package com.qeweb.scm.check.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
/**
 * 
 * @author VILON
 *
 */
@Entity
@Table(name = "qeweb_check_ex")
public class CheckExceptionEntity extends BaseEntity {
	/**
	 * 对账单明细
	 */
	private CheckItemEntity checkItem;
	/**
	 * 冗余字段
	 */
	private Double buyerCheckPrice;
	private String exDiscription;
	private String materialCode;	//零件编号
	private String materialName;	//零件名称
	private Double receiveQty;	//数量
	private String unitName;	//单位
	private Double checkPrice; //核价单价
	private String deliveryCode;//发货单号
	private String returnCode;//退货单号
	

	@ManyToOne
	@JoinColumn(name="check_item_id")
	public CheckItemEntity getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(CheckItemEntity checkItem) {
		this.checkItem = checkItem;
	}
	@Transient
	public Double getBuyerCheckPrice() {
		return buyerCheckPrice;
	}

	public void setBuyerCheckPrice(Double buyerCheckPrice) {
		this.buyerCheckPrice = buyerCheckPrice;
	}
	@Transient
	public String getExDiscription() {
		return exDiscription;
	}

	public void setExDiscription(String exDiscription) {
		this.exDiscription = exDiscription;
	}
	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	@Transient
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	@Transient
	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	@Transient
	public Double getCheckPrice() {
		return checkPrice;
	}

	public void setCheckPrice(Double checkPrice) {
		this.checkPrice = checkPrice;
	}
	@Transient
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	@Transient
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	
	
}