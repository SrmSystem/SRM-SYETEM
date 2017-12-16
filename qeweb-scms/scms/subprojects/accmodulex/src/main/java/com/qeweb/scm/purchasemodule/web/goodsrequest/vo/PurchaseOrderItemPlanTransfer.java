package com.qeweb.scm.purchasemodule.web.goodsrequest.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;

public class PurchaseOrderItemPlanTransfer {

	@ExcelCol(column = Column.B, required = true, desc="采购订单号")   
	private String orderCode;
	
	@ExcelCol(column = Column.C, desc="采购组")
	private String groupName;
	
	@ExcelCol(column = Column.D, desc="行号")
	private String itemNo;
	
	@ExcelCol(column = Column.E, desc="工厂名称")
	private String factoryName;
	
	@ExcelCol(column = Column.F, desc="物料号")
	private String materialName;
	
	@ExcelCol(column = Column.G, desc="基本单位")
	private String unitName;
	
	@ExcelCol(column = Column.H, desc="匹配订单数量")
	private String orderQty;
	
	@ExcelCol(column = Column.I, desc="匹配基本数量")
	private String baseQty;
	
	@ExcelCol(column = Column.J, desc="预计到货日期")
	private String rq;
	
	@ExcelCol(column = Column.K, desc="物流天数")
	private String ysts;
	
	@ExcelCol(column = Column.L, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.M, desc="供应商编码")
	private String vendorCode;

	@ExcelCol(column = Column.N, desc="发布状态")
	private String publishStatus;
	
	@ExcelCol(column = Column.O, desc="确认状态")
	private String confirmStatus;
	
	@ExcelCol(column = Column.P, desc="发货状态")
	private String deliveryStatus;
	
	@ExcelCol(column = Column.Q, desc="收货状态")
	private String receiveStatus;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}







	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getYsts() {
		return ysts;
	}

	public void setYsts(String ysts) {
		this.ysts = ysts;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getBaseQty() {
		return baseQty;
	}

	public void setBaseQty(String baseQty) {
		this.baseQty = baseQty;
	}

	public String getPublishStatus() {
		String status = "";
		if(publishStatus.equals("0")){
			status = "未发布";
		}else if(publishStatus.equals("1")){
			status = "已发布";
		}else if(publishStatus.equals("2")){
			status = "部分发布";
		}else{
			status = "";
		}
		return status;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getConfirmStatus() {
		String status = "";
		if(confirmStatus.equals("0")){
			status = "待确认";
		}else if(confirmStatus.equals("1")){
			status = "已确认";
		}else if(confirmStatus.equals("2")){
			status = "部分确认";
		}else if(confirmStatus.equals("-1")){
			status = "驳回";
		}else if(confirmStatus.equals("-2")){
			status = "拒绝驳回";
		}else{
			status = "";
		}
		return status;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getDeliveryStatus() {
		String status = "";
		if(deliveryStatus.equals("0")){
			status = "未发货";
		}else if(deliveryStatus.equals("1")){
			status = "已发货";
		}else if(deliveryStatus.equals("2")){
			status = "部分发货";
		}else{
			status = "";
		}
		return status;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getReceiveStatus() {
		String status = "";
		if(receiveStatus.equals("0")){
			status = "未收货";
		}else if(receiveStatus.equals("1")){
			status = "已收货";
		}else if(receiveStatus.equals("2")){
			status = "部分收货";
		}else{
			status = "";
		}
		return status;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
}
