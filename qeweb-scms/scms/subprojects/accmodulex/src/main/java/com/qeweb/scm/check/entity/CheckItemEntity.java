package com.qeweb.scm.check.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
//import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
/**
 * 
 * @author VILON
 *
 */
@Entity
@Table(name = "qeweb_check_item")
public class CheckItemEntity extends BaseEntity {
	/**
	 * 对账单
	 */
	private CheckEntity check;
	/**
	 * 收货明细
	 */
	private ReceiveItemEntity recItem;
	/**
	 * 退货明细
	 */
	private PurchaseOrderItemEntity retItem;
	/**
	 * 发票
	 */
	private CheckInvoiceEntity invoice;
	/**
	 * 核价价格
	 */
	private Double checkPrice;
	/**
	 * 供应商核对价格
	 */
	private Double vendorCheckPrice;
	/**
	 * 采购组织核对价格
	 */
	private Double buyerCheckPrice;
	/**
	 * 差异价格
	 */
	private Double exPrice;
	/**
	 * 异常状态
	 */
	private Integer exStatus;
	/**
	 * 异常描述
	 */
	private String exDiscription;
	/**
	 * 异常处理状态
	 */
	private Integer exDealStatus;
	/**
	 * 异常确认状态
	 */
	private Integer exConfirmStatus;
	
	private String col1;			//税金		 
	private String col2;			 
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	
	//add by chao.gu 20170710 对账明细来源 1：收货   -1：退货
	private int source;
	//add end
	//add by chao.gu 20170711 采购订单号
	private String orderCode;
	private Integer itemNo;	//行号
	private Double zhd;//含税价，从sap获得
	private MaterialEntity material;		// 物料
	//add end
	
	//非持久化字段
	private Timestamp receiveTime;
	private String qadCode;
	private String materialCode;
	private String materialName;
	private Double receiveQty;
	private String unitName;
	private String price;
	private String deliveryCode;
	private String vendorBatchNum;
	private int orderType;
	private String receiveCode;
	private String retrunCode;//退货单号
	private Timestamp returnTime;//退货时间
	
	
	private long viewNoCheckItemId;
	
	@Column(name="VIEW_NO_CHECK_ITEM_ID")
	public long getViewNoCheckItemId() {
		return viewNoCheckItemId;
	}
	public void setViewNoCheckItemId(long viewNoCheckItemId) {
		this.viewNoCheckItemId = viewNoCheckItemId;
	}
	
	@ManyToOne
	@JoinColumn(name="check_id")
	public CheckEntity getCheck() {
		return check;
	}
	public void setCheck(CheckEntity check) {
		this.check = check;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="rec_item_id")
	public ReceiveItemEntity getRecItem() {
		return recItem;
	}
	public void setRecItem(ReceiveItemEntity recItem) {
		this.recItem = recItem;
	}
	
	
	@ManyToOne
	@JoinColumn(name="invoice_id")
	public CheckInvoiceEntity getInvoice() {
		return invoice;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ret_item_id")
	public PurchaseOrderItemEntity getRetItem() {
		return retItem;
	}
	public void setRetItem(PurchaseOrderItemEntity retItem) {
		this.retItem = retItem;
	}
	public void setInvoice(CheckInvoiceEntity invoice) {
		this.invoice = invoice;
	}
	@Column(name="check_price")
	public Double getCheckPrice() {
		return checkPrice;
	}
	public void setCheckPrice(Double checkPrice) {
		this.checkPrice = checkPrice;
	}
	@Column(name="vendor_price")
	public Double getVendorCheckPrice() {
		return vendorCheckPrice;
	}
	public void setVendorCheckPrice(Double vendorCheckPrice) {
		this.vendorCheckPrice = vendorCheckPrice;
	}
	@Column(name="buyer_check_price")
	public Double getBuyerCheckPrice() {
		return buyerCheckPrice;
	}
	public void setBuyerCheckPrice(Double buyerCheckPrice) {
		this.buyerCheckPrice = buyerCheckPrice;
	}
	@Column(name="ex_status")
	public Integer getExStatus() {
		return exStatus;
	}
	public void setExStatus(Integer exStatus) {
		this.exStatus = exStatus;
	}
	@Column(name="ex_discription")
	public String getExDiscription() {
		return exDiscription;
	}
	public void setExDiscription(String exDiscription) {
		this.exDiscription = exDiscription;
	}
	@Column(name="ex_deal_status")
	public Integer getExDealStatus() {
		return exDealStatus;
	}
	public void setExDealStatus(Integer exDealStatus) {
		this.exDealStatus = exDealStatus;
	}
	@Column(name="ex_confirm_status")
	public Integer getExConfirmStatus() {
		return exConfirmStatus;
	}
	public void setExConfirmStatus(Integer exConfirmStatus) {
		this.exConfirmStatus = exConfirmStatus;
	}
	@Column(name="ex_price")
	public Double getExPrice() {
		return exPrice;
	}
	public void setExPrice(Double exPrice) {
		this.exPrice = exPrice;
	}
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	public String getCol4() {
		return col4;
	}
	public void setCol4(String col4) {
		this.col4 = col4;
	}
	public String getCol5() {
		return col5;
	}
	public void setCol5(String col5) {
		this.col5 = col5;
	}
	public String getCol6() {
		return col6;
	}
	public void setCol6(String col6) {
		this.col6 = col6;
	}
	public String getCol7() {
		return col7;
	}
	public void setCol7(String col7) {
		this.col7 = col7;
	}
	public String getCol8() {
		return col8;
	}
	public void setCol8(String col8) {
		this.col8 = col8;
	}
	public String getCol9() {
		return col9;
	}
	public void setCol9(String col9) {
		this.col9 = col9;
	}
	public String getCol10() {
		return col10;
	}
	public void setCol10(String col10) {
		this.col10 = col10;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	@Transient
	public String getQadCode() {
		return qadCode;
	}
	public void setQadCode(String qadCode) {
		this.qadCode = qadCode;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Transient
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	@Transient
	public String getVendorBatchNum() {
		return vendorBatchNum;
	}
	public void setVendorBatchNum(String vendorBatchNum) {
		this.vendorBatchNum = vendorBatchNum;
	}
	
	@Transient
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	
	
	@Transient
	public String getReceiveCode() {
		return receiveCode;
	}
	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}
	
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public Double getZhd() {
		return zhd;
	}
	public void setZhd(Double zhd) {
		this.zhd = zhd;
	}
	
	@Transient
	public String getRetrunCode() {
		return retrunCode;
	}
	public void setRetrunCode(String retrunCode) {
		this.retrunCode = retrunCode;
	}
	
	@Transient
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	public Timestamp getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}
	
	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	
}