package com.qeweb.scm.vendormodule.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;
/**
 * 供货关系导入导出数据存放
 * @author lw
 * 创建时间：2015年7月20日15:52:38
 * 最后更新时间：2015年7月20日15:52:42
 * 最后更新人：lw
 */
@ExcelTransfer(start=1, describe = "供货关系") 
public class VendorMaterialRelTransfer2 {
	@ExcelCol(column = Column.B,desc="供应商编码")
	private String vendorCode;
	@ExcelCol(column = Column.C,desc="供应商简称")
	private String vendorName;
	@ExcelCol(column = Column.D,desc="零部件编号")
	private String partsCode;
	@ExcelCol(column = Column.E,desc="零部件名称")
	private String partsName;
	@ExcelCol(column = Column.F, desc="供货系数")
	private String supplyCoefficient;
	@ExcelCol(column = Column.G, desc="是否主供")
	private String ismain;
	@ExcelCol(column = Column.H, desc="事业部代码")
	private String buCode;
	@ExcelCol(column = Column.I, desc="事业部名称")
	private String buName;
	@ExcelCol(column = Column.J, desc="品牌代码")
	private String brandCode;
	@ExcelCol(column = Column.K, desc="品牌名称")
	private String brandName;
	@ExcelCol(column = Column.L, desc="生效日期")
	private String createTime;
	@ExcelCol(column = Column.M, desc="产品线代码")
	private String productLineCode;
	@ExcelCol(column = Column.N, desc="产品线")
	private String productLineName;
	@ExcelCol(column = Column.O, desc="备注")
	private String bz;
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
	public String getPartsCode() {
		return partsCode;
	}
	public void setPartsCode(String partsCode) {
		this.partsCode = partsCode;
	}
	public String getPartsName() {
		return partsName;
	}
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	public String getSupplyCoefficient() {
		return supplyCoefficient;
	}
	public void setSupplyCoefficient(String supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	public String getBuCode() {
		return buCode;
	}
	public void setBuCode(String buCode) {
		this.buCode = buCode;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
}
