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
public class VendorMaterialRelTransfer {
	@ExcelCol(column = Column.B, required = true,desc="采购组织编码")
	private String buyerCode;
	@ExcelCol(column = Column.C, required = true,desc="采购组织名称")
	private String buyerName;
	@ExcelCol(column = Column.D, required = true,desc="供应商编码")
	private String vendorCode;
	@ExcelCol(column = Column.E, required = true,desc="供应商名称")
	private String vendorName;
	@ExcelCol(column = Column.F, required = true,desc="物料图号")
	private String materialCode;
	@ExcelCol(column = Column.G, required = true,desc="物料名称")
	private String materialName;
	@ExcelCol(column = Column.H, desc="供货状态")
	private String status;
	@ExcelCol(column = Column.I, desc="标杆车型")
	private String carModel;//标杆车型
	@ExcelCol(column = Column.J, desc="数据来源")
	private String dataFrom;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	public String getBuyerCode() {
		return buyerCode;
	}
	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	
}
