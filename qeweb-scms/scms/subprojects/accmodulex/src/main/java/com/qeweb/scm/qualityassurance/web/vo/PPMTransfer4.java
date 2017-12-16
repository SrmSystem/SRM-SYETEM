package com.qeweb.scm.qualityassurance.web.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "售后PPM") 
public class PPMTransfer4 {

	@ExcelCol(column = Column.B, desc="月份")
	private String month;
	
	@ExcelCol(column = Column.C, desc="供应商编码")
	private String vendorCode;
	
	@ExcelCol(column = Column.D, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.E, desc="物料图号")
	private String materialCode;
	
	@ExcelCol(column = Column.F, desc="物料名称")
	private String materialName;
	
	@ExcelCol(column = Column.G, desc="PPM值")
	private String ppm;

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

	public String getPpm() {
		return ppm;
	}

	public void setPpm(String ppm) {
		this.ppm = ppm;
	}

}
