package com.qeweb.scm.basemodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "协议价格管理") 
public class PriceNegotiatedVO{

	@ExcelCol(column=Column.A,required=true)
	private String materialCode;//物料COde
	
	
	@ExcelCol(column=Column.B,required=true)
	private String materialName;//物理名称

	
	@ExcelCol(column=Column.C,required=true)
	private String vendorCode;//供应商Code	
	
	@ExcelCol(column=Column.D,required=true)
	private String vendorName;//供应商名称	
	
	@ExcelCol(column=Column.E,required=true)
	private String negotiatedPrices;//协议价格
	
	@ExcelCol(column=Column.F,required=true)
	private String negotiatedUnit;//协议单位
	
	@ExcelCol(column=Column.G,required=true)
	private String negotiatedCurrency;//协议币种
	
	@ExcelCol(column=Column.H,required=true)
	private String negotiatedStartDate;
	
	@ExcelCol(column=Column.I,required=true)
	private String negotiatedEndDate;

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

	public String getNegotiatedPrices() {
		return negotiatedPrices;
	}

	public void setNegotiatedPrices(String negotiatedPrices) {
		this.negotiatedPrices = negotiatedPrices;
	}

	public String getNegotiatedUnit() {
		return negotiatedUnit;
	}

	public void setNegotiatedUnit(String negotiatedUnit) {
		this.negotiatedUnit = negotiatedUnit;
	}

	public String getNegotiatedCurrency() {
		return negotiatedCurrency;
	}

	public void setNegotiatedCurrency(String negotiatedCurrency) {
		this.negotiatedCurrency = negotiatedCurrency;
	}

	public String getNegotiatedStartDate() {
		return negotiatedStartDate;
	}

	public void setNegotiatedStartDate(String negotiatedStartDate) {
		this.negotiatedStartDate = negotiatedStartDate;
	}

	public String getNegotiatedEndDate() {
		return negotiatedEndDate;
	}

	public void setNegotiatedEndDate(String negotiatedEndDate) {
		this.negotiatedEndDate = negotiatedEndDate;
	}
}
