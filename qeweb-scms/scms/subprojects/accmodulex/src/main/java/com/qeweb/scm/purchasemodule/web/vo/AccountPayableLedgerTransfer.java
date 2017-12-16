package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "应付款台账") 
public class AccountPayableLedgerTransfer {

	@ExcelCol(column = Column.B, required=true, desc="采方编码")
	private String buyerCode;
	
	@ExcelCol(column = Column.C, desc="采方名称")
	private String buyerName;
	
	@ExcelCol(column = Column.D, required=true, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.E, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.F, desc="付款周期")
	private String paymentCycle;
	
	@ExcelCol(column = Column.G, desc="30天内")
	private String less30;					 
	
	@ExcelCol(column = Column.H, desc="30到60天")
	private String bet30and60;			 
	
	@ExcelCol(column = Column.I, desc="60到90天")
	private String bet60and90;				 
	
	@ExcelCol(column = Column.J, desc="90天以上")
	private String greater90;			 
	
	@ExcelCol(column = Column.K, desc="应付合计")
	private String totalPrice;				 
	
	@ExcelCol(column = Column.L, desc="批准金额")
	private String approvePrice;

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

	public String getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public String getLess30() {
		return less30;
	}

	public void setLess30(String less30) {
		this.less30 = less30;
	}

	public String getBet30and60() {
		return bet30and60;
	}

	public void setBet30and60(String bet30and60) {
		this.bet30and60 = bet30and60;
	}

	public String getBet60and90() {
		return bet60and90;
	}

	public void setBet60and90(String bet60and90) {
		this.bet60and90 = bet60and90;
	}

	public String getGreater90() {
		return greater90;
	}

	public void setGreater90(String greater90) {
		this.greater90 = greater90;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getApprovePrice() {
		return approvePrice;
	}

	public void setApprovePrice(String approvePrice) {
		this.approvePrice = approvePrice;
	}	
	
}
