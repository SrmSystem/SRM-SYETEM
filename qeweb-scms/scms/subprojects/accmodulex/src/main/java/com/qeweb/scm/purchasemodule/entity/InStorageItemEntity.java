package com.qeweb.scm.purchasemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * 入库明细
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_in_storage_item")
public class InStorageItemEntity extends BaseEntity {

	private InStorageEntity inStorage;		// 入库主单
	private Integer itemNo;					// 行号
	private MaterialEntity material;		// 物料
	private Double inStorageQty;			// 入库数量
	private Double inAccountQty;			// 结算数量
	private Double price;					// 价格
	private String receiveOrg;				// 收货方
	private Timestamp inStorageTime;		// 入库时间
	private Integer invoiceStatus;			// 开票状态 0：开票未完成 1：开票完成
	private ReceiveItemEntity receiveItem;			    // 收货明细
	private Integer checkedStatus;//是否已对账 0未对账 1已对账
	//非持久化
	private Double accountQty;
	private String unitName;
	private String requestTime;
	private String deliveryCode;
	

	
	@ManyToOne
	@JoinColumn(name="receive_item_id")
	public ReceiveItemEntity getReceiveItem() {
		return receiveItem;
	}

	public void setReceiveItem(ReceiveItemEntity receiveItem) {
		this.receiveItem = receiveItem;
	}

	@ManyToOne
	@JoinColumn(name="in_storage_id")
	public InStorageEntity getInStorage() {
		return inStorage;
	}

	public void setInStorage(InStorageEntity inStorage) {
		this.inStorage = inStorage;
	}

	@Column(name="item_no")
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="in_storage_qty")
	public Double getInStorageQty() {
		return inStorageQty;
	}

	public void setInStorageQty(Double inStorageQty) {
		this.inStorageQty = inStorageQty;
	}

	@Column(name="in_account_qty")
	public Double getInAccountQty() {
		return inAccountQty;
	}

	public void setInAccountQty(Double inAccountQty) {
		this.inAccountQty = inAccountQty;
	}

	@Column(name="price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name="receive_org")
	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	@Column(name="in_storage_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getInStorageTime() {
		return inStorageTime;
	}

	public void setInStorageTime(Timestamp inStorageTime) {
		this.inStorageTime = inStorageTime;
	}

	@Transient
	public Double getAccountQty() {
		return accountQty;
	}

	public void setAccountQty(Double accountQty) {
		this.accountQty = accountQty;
	}

	@Column(name="invoice_status")
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}


	public Integer getCheckedStatus() {
		return checkedStatus;
	}

	public void setCheckedStatus(Integer checkedStatus) {
		this.checkedStatus = checkedStatus;
	}
	@Transient
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	@Transient
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	@Transient
	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	
	

	
}
