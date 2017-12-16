package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;

/**
 * (国内、外协)供方对账明细导出类
 * @author zhangjiejun
 */
@ExcelTransfer(start=1, describe = "供方对账明细") 
public class CheckItemInVendorTransfer {
	
	@ExcelCol(type = ColType.DATE, column = Column.B, desc="收货时间")	
	private String receiveTime;

	@ExcelCol(column = Column.C, required = true, desc="收货单号")   
	private String qadCode;
	
	@ExcelCol(column = Column.D, desc="数量")
	private String receiveQty;
	
	@ExcelCol(column = Column.E, required = true, desc="发票号")
	private String invoiceCode;
	
	@ExcelCol(column = Column.F, desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column = Column.G, required = true, desc="物料名称")
	private String materialName;
	
	@ExcelCol(column = Column.H, desc="单位")
	private String unitName;

	@ExcelCol(column = Column.I, desc="供方填写单价")
	private String vendorCheckPrice;
	
	@ExcelCol(column = Column.J, desc="采购员核对金额")
	private String buyerCheckPrice;
	
	@ExcelCol(column = Column.K, desc="税金")
	private String col1;
	
	@ExcelCol(column = Column.L, desc="差异状态")
	private String exStatus;
	
	@ExcelCol(column = Column.M, desc="差异说明")
	private String exDiscription;
	
	@ExcelCol(column = Column.N, desc="差异处理状态")
	private String exDealStatus;
	
	@ExcelCol(column = Column.O, desc="差异处理状态")
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

	public String getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(String receiveQty) {
		this.receiveQty = receiveQty;
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