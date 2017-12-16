package com.qeweb.scm.vendorperformancemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "采购额数据导入") 
public class VendorPerforPurchasedatainVo{
	
	@ExcelCol(column = Column.A, required = true,desc="供应商编码")
	private String vendorCode;//供应商Code	
	
	@ExcelCol(column = Column.B,desc="供应商名称")
	private String vendorName;//供应商名称	
	
	@ExcelCol(column = Column.C, required = true,desc="品牌编码")
	private String brandCode;//品牌编码
	
	@ExcelCol(column = Column.D, required = true,desc="品牌名称")
	private String brandName;//品牌名称	
	
	@ExcelCol(column = Column.E, required = true,desc="供应商月份")
	private String vendorDate;//供应商月份
	
	@ExcelCol(column = Column.F, required = true,desc="采购额")
	private String defaultPurchase;//采购额

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getVendorDate() {
		return vendorDate;
	}

	public void setVendorDate(String vendorDate) {
		this.vendorDate = vendorDate;
	}

	public String getDefaultPurchase() {
		return defaultPurchase;
	}

	public void setDefaultPurchase(String defaultPurchase) {
		this.defaultPurchase = defaultPurchase;
	}
}
