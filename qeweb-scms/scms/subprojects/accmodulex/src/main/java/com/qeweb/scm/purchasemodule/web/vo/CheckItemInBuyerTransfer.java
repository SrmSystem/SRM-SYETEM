package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;

/**
 * (国内、外协)采方对账明细导出类
 * @author zhangjiejun
 */
@ExcelTransfer(start=1, describe = "采方对账明细") 
public class CheckItemInBuyerTransfer {
	
	@ExcelCol(type = ColType.DATE, column = Column.B, desc="收货日期")	
	private String receiveTime;

	@ExcelCol(column = Column.C, required = true, desc="收货单号")   
	private String qadCode;
	
	@ExcelCol(column = Column.D, required = true, desc="对应发票号")
	private String invoiceCode;
	
	@ExcelCol(column = Column.E, desc="零件号")
	private String materialCode;

	@ExcelCol(column = Column.F, desc="数量")
	private String receiveQty;
	
	@ExcelCol(column = Column.G, desc="单位")
	private String unitName;
	
	@ExcelCol(column = Column.H, desc="供方填写单价")
	private String vendorCheckPrice;
	
	@ExcelCol(column = Column.I, desc="采购员核对金额")
	private String buyerCheckPrice;
	
	@ExcelCol(column = Column.J, desc="核价价格")
	private String recItemAttr3;
	
	@ExcelCol(column = Column.K, desc="ASN号")
	private String deliveryCode;
	
	@ExcelCol(column = Column.L, desc="供应商批次号")
	private String recItemAttr5;
	
	@ExcelCol(column = Column.M, desc="税金")
	private String col1;
	
	@ExcelCol(column = Column.N, desc="差异状态")
	private String exStatus;
	
	@ExcelCol(column = Column.O, desc="差异说明")
	private String exDiscription;
	
	@ExcelCol(column = Column.P, desc="差异处理状态")
	private String exDealStatus;
	
	@ExcelCol(column = Column.Q, desc="差异处理状态")
	private String exConfirmStatus;
	
	public String getReceiveTime() {
		if(!CommonUtil.isNotNullAndNotEmpty(receiveTime)){
			receiveTime = "";
		}
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getQadCode() {
		return qadCode;
	}

	public void setQadCode(String qadCode) {
		this.qadCode = qadCode;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(String receiveQty) {
		this.receiveQty = receiveQty;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getVendorCheckPrice() {
		return vendorCheckPrice;
	}

	public void setVendorCheckPrice(String vendorCheckPrice) {
		this.vendorCheckPrice = vendorCheckPrice;
	}

	public String getBuyerCheckPrice() {
		return buyerCheckPrice;
	}

	public void setBuyerCheckPrice(String buyerCheckPrice) {
		this.buyerCheckPrice = buyerCheckPrice;
	}

	public String getRecItemAttr3() {
		return recItemAttr3;
	}

	public void setRecItemAttr3(String recItemAttr3) {
		this.recItemAttr3 = recItemAttr3;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getRecItemAttr5() {
		return recItemAttr5;
	}

	public void setRecItemAttr5(String recItemAttr5) {
		this.recItemAttr5 = recItemAttr5;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getExStatus() {
		return exStatus;
	}

	public void setExStatus(String exStatus) {
		this.exStatus = exStatus;
	}

	public String getExDiscription() {
		return exDiscription;
	}

	public void setExDiscription(String exDiscription) {
		this.exDiscription = exDiscription;
	}

	public String getExDealStatus() {
		return exDealStatus;
	}

	public void setExDealStatus(String exDealStatus) {
		this.exDealStatus = exDealStatus;
	}

	public String getExConfirmStatus() {
		return exConfirmStatus;
	}

	public void setExConfirmStatus(String exConfirmStatus) {
		this.exConfirmStatus = exConfirmStatus;
	}

}