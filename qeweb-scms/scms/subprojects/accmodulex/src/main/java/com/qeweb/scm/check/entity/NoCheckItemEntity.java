package com.qeweb.scm.check.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.IdEntity;
/**
 * 
 * @author u
 * 未对账明细(视图)
 */
@Entity
@Table(name = "VIEW_NO_CHECK_ITEM")
public class NoCheckItemEntity extends IdEntity{

	private String dlvCode;				//发货单号
	private String buyerCode;			//采购组织编码
	private String buyerName;			//采购组织名称
	private String vendorCode;			//供应商编码
	private String vendorName;			//供应商名称
	private String recCode;				//收货单号
	private Double recQty;				//收货数量
	private String returnCode;			//退货单号
	private Timestamp returnTime;		//退货时间
	private String orderCode;			//订单号
	private Integer itemNo;				//订单行
	private String materialCode;		//物料编码
	private String materialName;		//物料名称
	private String unitName;			//单位
	private Timestamp createTime;		//创建时间
	private Long recItemId;				//收货明细ID
	private Long recId;					//收货单ID
	private Long dlvItemId;				//发货明细ID
	private Long dlvId;					//发货单ID
	private Long orderItemId;			//订单明细ID
	private Long orderId;				//订单ID
	private Long materialId;			//物料ID
	private Long buyerId;				//采购组织ID
	private Long vendorId;				//供应商ID
	
	private Integer dataType;			//数据类型 1=收货单，-1=退货单
	
	@Column(name="DLV_CODE")
	public String getDlvCode() {
		return dlvCode;
	}
	public void setDlvCode(String dlvCode) {
		this.dlvCode = dlvCode;
	}
	
	@Column(name="BUYER_CODE")
	public String getBuyerCode() {
		return buyerCode;
	}
	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}
	
	@Column(name="BUYER_NAME")
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@Column(name="VENDOR_CODE")
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	@Column(name="VENDOR_NAME")
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	@Column(name="REC_CODE")
	public String getRecCode() {
		return recCode;
	}
	public void setRecCode(String recCode) {
		this.recCode = recCode;
	}
	
	@Column(name="REC_QTY")
	public Double getRecQty() {
		return recQty;
	}
	public void setRecQty(Double recQty) {
		this.recQty = recQty;
	}
	
	@Column(name="RETURN_CODE")
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	
	@Column(name="RETURN_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}
	
	@Column(name="ORDER_CODE")
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@Column(name="ITEM_NO")
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	
	@Column(name="MATERIAL_CODE")
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Column(name="MATERIAL_NAME")
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@Column(name="UNIT_NAME")
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@Column(name="CREATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="REC_ITEM_ID")
	public Long getRecItemId() {
		return recItemId;
	}
	public void setRecItemId(Long recItemId) {
		this.recItemId = recItemId;
	}
	
	@Column(name="ORDER_ITEM_ID")
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	
	@Column(name="BUYER_ID")
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	
	@Column(name="VENDOR_ID")
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	@Column(name="REC_ID")
	public Long getRecId() {
		return recId;
	}
	public void setRecId(Long recId) {
		this.recId = recId;
	}
	
	@Column(name="DLV_ITEM_ID")
	public Long getDlvItemId() {
		return dlvItemId;
	}
	public void setDlvItemId(Long dlvItemId) {
		this.dlvItemId = dlvItemId;
	}
	
	@Column(name="DLV_ID")
	public Long getDlvId() {
		return dlvId;
	}
	public void setDlvId(Long dlvId) {
		this.dlvId = dlvId;
	}
	
	@Column(name="ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="MATERIAL_ID")
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	
	@Column(name="DATA_TYPE")
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	
	
	
	
	
}