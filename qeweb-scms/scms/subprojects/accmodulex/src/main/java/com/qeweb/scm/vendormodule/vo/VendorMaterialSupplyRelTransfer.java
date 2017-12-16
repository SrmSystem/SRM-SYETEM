package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
/**
 * 供货系数导入数据存放
 * @author lw
 * 创建时间：2015年6月29日10:22:19
 * 最后更新时间：2015年6月30日09:42:57
 * 最后更新人：lw
 */
@ExcelTransfer(start=1, describe = "供货系数") 
public class VendorMaterialSupplyRelTransfer {
	
	@ExcelCol(column = Column.B, required = true,desc="物料编码")
	private String materialCode;
	@ExcelCol(column = Column.C, required = true,desc="物料名称")
	private String materialName;
	@ExcelCol(column = Column.D, required = true,desc="业务范围编号")
	private String bussinessRangeCode;
	@ExcelCol(column = Column.E, required = true,desc="业务范围名称")
	private String bussinessRangeName;
	@ExcelCol(column = Column.F, required = true,desc="业务类型编号")
	private String bussinessCode;
	@ExcelCol(column = Column.G, required = true,desc="业务类型名称")
	private String bussinessName;
	@ExcelCol(column = Column.H, required = true,desc="品牌编号")
	private String brandCode;
	@ExcelCol(column = Column.I, required = true,desc="品牌名称")
	private String brandName;
	@ExcelCol(column = Column.J, required = true,desc="产品线编号")
	private String productLineCode;
	@ExcelCol(column = Column.K, required = true,desc="产品线名称")
	private String productLineName;
	@ExcelCol(column = Column.L, required = true,desc="工厂编号")
	private String factoryCode;
	@ExcelCol(column = Column.M, required = true,desc="工厂名称")
	private String factoryName;
	@ExcelCol(column = Column.N, required = true,desc="是否主供")
	private String ismain;
	@ExcelCol(type = ColType.NUMBER, column = Column.O,desc="供货系数")
	private String supplyCoefficient;
	@ExcelCol(column = Column.P, required = true,desc="生效时间")
	private String effectiveTime;
	
	
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
	public String getBussinessRangeCode() {
		return bussinessRangeCode;
	}
	public void setBussinessRangeCode(String bussinessRangeCode) {
		this.bussinessRangeCode = bussinessRangeCode;
	}
	public String getBussinessRangeName() {
		return bussinessRangeName;
	}
	public void setBussinessRangeName(String bussinessRangeName) {
		this.bussinessRangeName = bussinessRangeName;
	}
	public String getBussinessCode() {
		return bussinessCode;
	}
	public void setBussinessCode(String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}
	public String getBussinessName() {
		return bussinessName;
	}
	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getProductLineCode() {
		return productLineCode;
	}
	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	public String getSupplyCoefficient() {
		return supplyCoefficient;
	}
	public void setSupplyCoefficient(String supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
}
