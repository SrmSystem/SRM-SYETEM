package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
@ExcelTransfer(start=1, describe = "供应商品牌重合度统计") 
public class StatisticsVendorBrandOverlapTransfer {
	
	@ExcelCol(column = Column.B, required = true,desc="供应商名称")
	private String vendorName;
	@ExcelCol(column = Column.C, required = true,desc="供货品牌")
	private String brandName;
	@ExcelCol(type = ColType.NUMBER,column = Column.D, required = true,desc="供货品牌数量")
	private String brandCount;
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
	public String getBrandCount() {
		return brandCount;
	}
	public void setBrandCount(String brandCount) {
		this.brandCount = brandCount;
	}
	
	
	
}
