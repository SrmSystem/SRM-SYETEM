package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "采购计划") 
public class PurchasePlanTransfer {

	@ExcelCol(column = Column.B, desc="月份")   
	private String month;
	
	@ExcelCol(column = Column.C, required = true, desc="采购商编码")
	private String buyerCode;
	
	@ExcelCol(column = Column.D, desc="采购商名称")
	private String buyerName;
	
	@ExcelCol(column = Column.E, required = true, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.F, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.G, required = true, desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column = Column.H, desc="物料名称")
	private String materialName;  
	
	@ExcelCol(type = ColType.NUMBER, column = Column.I, desc="行号")
	private String itemNo;
	
	@ExcelCol(type = ColType.NUMBER, column = Column.J, desc="需求数量")
	private String totalPlanQty;
	
	@ExcelCol(type = ColType.DATE, column = Column.K, desc="需求时间") 
	private String planRecTime;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public String getTotalPlanQty() {
		return totalPlanQty;
	}

	public void setTotalPlanQty(String totalPlanQty) {
		this.totalPlanQty = totalPlanQty;
	}

	public String getPlanRecTime() {
		return planRecTime;
	}

	public void setPlanRecTime(String planRecTime) {
		this.planRecTime = planRecTime;
	}
	
}
