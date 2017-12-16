package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

/**
 * 订单明细导出类
 * @author zhangjiejun
 */
@ExcelTransfer(start=1, describe = "采购订单明细") 
public class PurchaseOrderItemTransfer {

	@ExcelCol(column = Column.B, required = true, desc="采购订单号")   
	private String orderCode;
	
	@ExcelCol(column = Column.C, desc="行号")
	private String itemNo;
	
	@ExcelCol(column = Column.D, desc="订单类型")
	private String purOrderType;
	
	@ExcelCol(column = Column.E, desc="公司编码")
	private String companyCode;
	
	@ExcelCol(column = Column.F, desc="公司名称")
	private String companyName;
	
	@ExcelCol(column = Column.G, desc="工厂编码")
	private String factoryCode;
	
	@ExcelCol(column = Column.H, desc="工厂名称")
	private String factoryName;
	
	@ExcelCol(column = Column.I, desc="项目类型")
	private String pstyp;
	
	@ExcelCol(column = Column.J, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.K, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.L, desc="付款条件")
	private String ztermms;
	
	@ExcelCol(column = Column.M, desc="物料号")
	private String materialCode;
	
	@ExcelCol(column = Column.N, desc="物料描述")
	private String materialDescride;
	
	@ExcelCol(column = Column.O, desc="订单数量")
	private String orderQty;
	
	@ExcelCol(column = Column.P, desc="送货数量")
	private String deQty;
	
	@ExcelCol(column = Column.Q, desc="在途数量")
	private String onwayQty;
	
	@ExcelCol(column = Column.R, desc="收货数量")
	private String receiveQty;

	@ExcelCol(column = Column.S, desc="来料不良数量")
	private String incomBadQty;
	
	@ExcelCol(column = Column.T, desc="送检不良数量")
	private String insBadQty;
	
	@ExcelCol(column = Column.U, desc="未发货数量")
	private String undeliveryQty;
	
	@ExcelCol(column = Column.V, desc="币种")
	private String currency;
	
	@ExcelCol(column = Column.W, desc="单位")
	private String unitName;

	@ExcelCol(column = Column.X, desc="价格")
	private String price;
	
	@ExcelCol(type = ColType.DATE, column = Column.Y, desc="订单审批日期")
	private String orderDate;
	
	@ExcelCol(type = ColType.DATE, column = Column.Z, desc="交货日期")
	private String requestTime;
	
	@ExcelCol(type = ColType.DATE, column = Column.AA, desc="发布时间")
	private String publishTime;
	
	@ExcelCol(type = ColType.DATE, column = Column.AB, desc="确认日期")
	private String confirmTime;
	
	
	@ExcelCol(column = Column.AC, desc="发布状态")
	private String publishStatus;
	
	@ExcelCol(column = Column.AD, desc="确认状态")
	private String confirmStatus;
	
	@ExcelCol(column = Column.AE, desc="发货状态")
	private String deliveryStatus;
	
	@ExcelCol(column = Column.AF, desc="收货状态")
	private String receiveStatus;
	
	@ExcelCol(column = Column.AG, desc="锁定状态")
	private String zlock;
	
	@ExcelCol(column = Column.AH, desc="关闭状态")
	private String closeStatus;
	
	@ExcelCol( column = Column.AI, desc="收货地址")
	private String receiveOrg;
	
	@ExcelCol(column = Column.AJ, desc="驳回原因")
	private String rejectReason;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getPurOrderType() {
		return purOrderType;
	}

	public void setPurOrderType(String purOrderType) {
		this.purOrderType = purOrderType;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getPstyp() {
		return pstyp;
	}

	public void setPstyp(String pstyp) {
		this.pstyp = pstyp;
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

	public String getZtermms() {
		return ztermms;
	}

	public void setZtermms(String ztermms) {
		this.ztermms = ztermms;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialDescride() {
		return materialDescride;
	}

	public void setMaterialDescride(String materialDescride) {
		this.materialDescride = materialDescride;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getDeQty() {
		return deQty;
	}

	public void setDeQty(String deQty) {
		this.deQty = deQty;
	}

	public String getOnwayQty() {
		return onwayQty;
	}

	public void setOnwayQty(String onwayQty) {
		this.onwayQty = onwayQty;
	}

	public String getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(String receiveQty) {
		this.receiveQty = receiveQty;
	}

	public String getIncomBadQty() {
		return incomBadQty;
	}

	public void setIncomBadQty(String incomBadQty) {
		this.incomBadQty = incomBadQty;
	}

	public String getInsBadQty() {
		return insBadQty;
	}

	public void setInsBadQty(String insBadQty) {
		this.insBadQty = insBadQty;
	}

	public String getUndeliveryQty() {
		return undeliveryQty;
	}

	public void setUndeliveryQty(String undeliveryQty) {
		this.undeliveryQty = undeliveryQty;
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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
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

	public String getZlock() {
		String status = "";
		if(zlock == null){
			status = "";
		}else{
			if(zlock.equals("0")){
				status = "未锁定";
			}else if(zlock.equals("1")){
				status = "已锁定";
			}else{
				status = "";
			}
		}
		return status;
	}

	public void setZlock(String zlock) {
		this.zlock = zlock;
	}

	public String getCloseStatus() {
		String status = "";
		if(closeStatus.equals("0")){
			status = "未关闭";
		}else if(closeStatus.equals("1")){
			status = "已关闭";
		}else{
			status = "";
		}
		return status;
	}

	public void setCloseStatus(String closeStatus) {
		this.closeStatus = closeStatus;
	}

	public String getReceiveOrg() {
		return receiveOrg;
	}

	public void setReceiveOrg(String receiveOrg) {
		this.receiveOrg = receiveOrg;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

}
