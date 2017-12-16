package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "采购删除计划") 
public class PurchasePlanDeleteTransfer {

	@ExcelCol(column = Column.B,  required = true, desc="工厂编码")   
	private String factoryCode;
	
	@ExcelCol(column = Column.C, required = true, desc="采购组编码")
	private String groupCode;
	
	@ExcelCol(column = Column.D, required = true, desc="供应商编码")
	private String vendorCode;
	
	
	@ExcelCol(column = Column.E, required = true, desc="物料号")
	private String materialCode;
	
	@ExcelCol( column = Column.F, desc="当日库存")
	private String inventory;
	
	@ExcelCol( column = Column.G, desc="当日未交po数量")
	private String deliveredCount ;

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getDeliveredCount() {
		return deliveredCount;
	}

	public void setDeliveredCount(String deliveredCount) {
		this.deliveredCount = deliveredCount;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}


	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
}
