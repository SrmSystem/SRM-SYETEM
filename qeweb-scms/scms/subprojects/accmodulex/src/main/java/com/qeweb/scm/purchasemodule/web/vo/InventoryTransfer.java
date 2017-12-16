package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "VMI库存") 
public class InventoryTransfer {

	@ExcelCol(column = Column.B, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.C, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.D, desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column = Column.E, desc="物料名称")
	private String materialName;  
	
	@ExcelCol(column = Column.F, desc="三方库存")
	private String stockQty;
	
	@ExcelCol(column = Column.G, desc="需求量（3天）")
	private String req3dayQty;
	
	@ExcelCol(column = Column.H, desc="需求量（当月）")
	private String reqMonthQty;
	
	@ExcelCol(column = Column.I, desc="最小库存")
	private String stockMinQty;

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

	public String getStockQty() {
		return stockQty;
	}

	public void setStockQty(String stockQty) {
		this.stockQty = stockQty;
	}

	public String getReq3dayQty() {
		return req3dayQty;
	}

	public void setReq3dayQty(String req3dayQty) {
		this.req3dayQty = req3dayQty;
	}

	public String getReqMonthQty() {
		return reqMonthQty;
	}

	public void setReqMonthQty(String reqMonthQty) {
		this.reqMonthQty = reqMonthQty;
	}

	public String getStockMinQty() {
		return stockMinQty;
	}

	public void setStockMinQty(String stockMinQty) {
		this.stockMinQty = stockMinQty;
	}
	
}
