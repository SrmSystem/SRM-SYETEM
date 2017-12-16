package com.qeweb.scm.purchasemodule.web.vo;

import java.sql.Timestamp;

/**
 *打印大包装小包装vo
 */
public class PrintDlvVO {
	private String vendorCode;//
	private String materialCode;//
	private String materialName;//
	private String materialVersion;//
	private String boxNum;//包装数
	private String vendorCharg;//追溯批号
	private String charg;//物料批号
	private String manufactureDate;//生产日期
	
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
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
	public String getMaterialVersion() {
		return materialVersion;
	}
	public void setMaterialVersion(String materialVersion) {
		this.materialVersion = materialVersion;
	}

	public String getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(String boxNum) {
		this.boxNum = boxNum;
	}
	public String getVendorCharg() {
		return vendorCharg;
	}
	public void setVendorCharg(String vendorCharg) {
		this.vendorCharg = vendorCharg;
	}
	public String getCharg() {
		return charg;
	}
	public void setCharg(String charg) {
		this.charg = charg;
	}
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	
	

}
