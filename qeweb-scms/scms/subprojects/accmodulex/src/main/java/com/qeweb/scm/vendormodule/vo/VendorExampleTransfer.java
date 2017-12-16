package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;



@ExcelTransfer(start=1, describe = "标杆企业供应商统计") 
public class VendorExampleTransfer {
	
	@ExcelCol(column = Column.A, required = true,desc="供应商编号")
	private String vendorCode;
	@ExcelCol(column = Column.B, required = true,desc="供应商名称")
	private String vendorName;
	@ExcelCol(column = Column.C, required = true,desc="业务类型")
	private String bussiness;
	@ExcelCol(column = Column.D, required = true,desc="品牌")
	private String brandName;
	@ExcelCol(column = Column.E, required = true,desc="产品线")
	private String productLineName;
	@ExcelCol(column = Column.F, required = true,desc="零部件名称")
	private String partsName;
	@ExcelCol(column = Column.G, required = true,desc="物料图号")
	private String materialCode;
	@ExcelCol(column = Column.H, required = true,desc="物料名称")
	private String materialName;
	@ExcelCol(column = Column.I, required = true,desc="系数")
	private String supplyCoefficient;
	@ExcelCol(column = Column.J, required = true,desc="标杆供应商车型及系数")
	private String biaoganSupplyCoefficient;
	
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
	public String getBussiness() {
		return bussiness;
	}
	public void setBussiness(String bussiness) {
		this.bussiness = bussiness;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
	public String getPartsName() {
		return partsName;
	}
	public void setPartsName(String partsName) {
		this.partsName = partsName;
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
	public String getSupplyCoefficient() {
		return supplyCoefficient;
	}
	public void setSupplyCoefficient(String supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}
	public String getBiaoganSupplyCoefficient() {
		return biaoganSupplyCoefficient;
	}
	public void setBiaoganSupplyCoefficient(String biaoganSupplyCoefficient) {
		this.biaoganSupplyCoefficient = biaoganSupplyCoefficient;
	}
}
