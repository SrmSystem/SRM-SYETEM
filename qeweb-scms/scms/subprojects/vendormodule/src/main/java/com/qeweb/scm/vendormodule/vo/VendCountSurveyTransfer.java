package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "供应商供货关系及系数统计") 
public class VendCountSurveyTransfer {

	@ExcelCol(column = Column.A, required = true,desc="供应商代码")
	private String code;
	@ExcelCol(column = Column.B, required = true,desc="供应商简称")
	private String shortName;
	@ExcelCol(column = Column.C, required = true,desc="邓白氏编码")
	private String duns;
	@ExcelCol(column = Column.D, required = true,desc="供应商性质")
	private String property;
	@ExcelCol(column = Column.E, required = true,desc="是否为上市公司")
	private String ipo;
	@ExcelCol(column = Column.F, required = true,desc="供应商名称")
	private String name;
	@ExcelCol(column = Column.G, required = true,desc="供应商阶段")
	private String vendorPhasName;
	@ExcelCol(column = Column.H, required = true,desc="供应商分类（A,B,C）")
	private String vendorClassify2;
	@ExcelCol(column = Column.I, required = true,desc="供应商等级")
	private String vendorLevel;
	@ExcelCol(column = Column.J, required = true,desc="供货图号")
	private String materialId;
	@ExcelCol(column = Column.K, required = true,desc="所属系统")
	private String xitong;
	@ExcelCol(column = Column.L, required = true,desc="供货品牌")
	private String brandName;
	@ExcelCol(column = Column.M, required = true,desc="供货工厂")
	private String factoryName;
	@ExcelCol(column = Column.N, required = true,desc="供货系数")
	private String supplyCoefficient;
	@ExcelCol(column = Column.O, required = true,desc="产品线")
	private String productLineName;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDuns() {
		return duns;
	}
	public void setDuns(String duns) {
		this.duns = duns;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getIpo() {
		return ipo;
	}
	public void setIpo(String ipo) {
		this.ipo = ipo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVendorPhasName() {
		return vendorPhasName;
	}
	public void setVendorPhasName(String vendorPhasName) {
		this.vendorPhasName = vendorPhasName;
	}
	public String getVendorClassify2() {
		return vendorClassify2;
	}
	public void setVendorClassify2(String vendorClassify2) {
		this.vendorClassify2 = vendorClassify2;
	}
	public String getVendorLevel() {
		return vendorLevel;
	}
	public void setVendorLevel(String vendorLevel) {
		this.vendorLevel = vendorLevel;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getXitong() {
		return xitong;
	}
	public void setXitong(String xitong) {
		this.xitong = xitong;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getSupplyCoefficient() {
		return supplyCoefficient;
	}
	public void setSupplyCoefficient(String supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
}
