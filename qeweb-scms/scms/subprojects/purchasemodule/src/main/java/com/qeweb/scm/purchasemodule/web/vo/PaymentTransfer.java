package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "月度付款") 
public class PaymentTransfer {

	@ExcelCol(column = Column.B, required=true, desc="采方编码")
	private String buyerCode;
	
	@ExcelCol(column = Column.C, desc="采方名称")
	private String buyerName;
	
	@ExcelCol(column = Column.D, required=true, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.E, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.F, required=true, desc="付款月份")
	private String payMonth;
	
	@ExcelCol(column = Column.G, type =ColType.NUMBER, required=true, desc="批准金额")
	private String approvedAmount;

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

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	
}
