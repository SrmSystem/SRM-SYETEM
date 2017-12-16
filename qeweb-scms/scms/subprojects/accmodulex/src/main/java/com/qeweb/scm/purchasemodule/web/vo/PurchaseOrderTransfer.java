package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "采购订单") 
public class PurchaseOrderTransfer {

	@ExcelCol(column = Column.B, required = true)   
	private String orderCode;
	
	@ExcelCol(column = Column.C, required = true)
	private String buyerCode;
	
	@ExcelCol(column = Column.D)
	private String buyerName;
	
	@ExcelCol(column = Column.E, required = true)
	private String vendorCode;
	
	@ExcelCol(column = Column.F)
	private String vendorName;
	
	@ExcelCol(column = Column.G)
	private String receiveOrg;
	
	@ExcelCol(column = Column.H)
	private String orderDate;
	
	@ExcelCol(column = Column.I, required = true)
	private String materialCode;
	
	@ExcelCol(column = Column.J)
	private String materialName;  
	
	@ExcelCol(type = ColType.NUMBER, column = Column.K)
	private String itemNo;
	
	@ExcelCol(column = Column.L)
	private String currency;
	
	@ExcelCol(column = Column.M)
	private String unitName;
	
	@ExcelCol(column = Column.N)
	private String price;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.O)
	private String orderQty;
	
	@ExcelCol(type = ColType.DATE, column = Column.P)
	private String requestTime;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
}