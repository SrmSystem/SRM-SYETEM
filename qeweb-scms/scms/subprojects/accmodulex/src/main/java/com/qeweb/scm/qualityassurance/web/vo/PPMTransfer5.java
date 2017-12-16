package com.qeweb.scm.qualityassurance.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "PPAP一次通过率") 
public class PPMTransfer5 {

	@ExcelCol(column = Column.B, desc="月份")
	private String month;
	
	@ExcelCol(column = Column.C, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.D, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.E, desc="一次通过率")
	private String rate;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

}
