package com.qeweb.scm.vendorperformancemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "数据导入") 
public class VendorPerforMATVo{
	
	@ExcelCol(column = Column.A, required = true,desc="物料类别编码")
	private String materialCode;//物料类别COde
	
	@ExcelCol(column = Column.B,desc="物料类别名称")
	private String materialName;//物理类别名称
	
	@ExcelCol(column = Column.C,desc="备注")
	private String rb;//备注

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

	public String getRb() {
		return rb;
	}

	public void setRb(String rb) {
		this.rb = rb;
	}
}
