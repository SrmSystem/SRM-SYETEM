package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "最小库存设置") 
public class MinInventorySettingTransfer {

	@ExcelCol(column = Column.B, required = true)
	private String vendorCode;
	
	@ExcelCol(column = Column.C)
	private String vendorName;
	
	@ExcelCol(column = Column.D, required = true)
	private String materialCode;
	
	@ExcelCol(column = Column.E)
	private String materialName;  
	
	@ExcelCol(type = ColType.NUMBER, column = Column.F, required = true)
	private String stockMinQty;
	
	@ExcelCol(column = Column.G)
	private String remark;

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

	public String getStockMinQty() {
		return stockMinQty;
	}

	public void setStockMinQty(String stockMinQty) {
		this.stockMinQty = stockMinQty;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
