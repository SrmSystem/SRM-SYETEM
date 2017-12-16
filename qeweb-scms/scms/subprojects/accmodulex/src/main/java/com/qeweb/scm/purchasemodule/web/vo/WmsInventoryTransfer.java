package com.qeweb.scm.purchasemodule.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "WMS库存") 
public class WmsInventoryTransfer {

	@ExcelCol(column = Column.B, desc="物料编码")
	private String materialCode;
	
	@ExcelCol(column = Column.C, desc="物料名称")
	private String materialName;  
	
	@ExcelCol(column = Column.D, desc="库存")  
	private String stockQty;

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

}
