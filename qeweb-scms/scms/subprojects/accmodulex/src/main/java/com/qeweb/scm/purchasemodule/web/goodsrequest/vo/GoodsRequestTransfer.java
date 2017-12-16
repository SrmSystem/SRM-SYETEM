package com.qeweb.scm.purchasemodule.web.goodsrequest.vo;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;

public class GoodsRequestTransfer {

	@ExcelCol(column = Column.B, required = true, desc="工厂代码")   
	private String factoryCode;
	
	@ExcelCol(column = Column.C, desc="工厂名称")
	private String factoryName;
	
	@ExcelCol(column = Column.D, desc="采购组代码")
	private String groupCode;
	
	@ExcelCol(column = Column.E, desc="采购组名称")
	private String groupName;
	
	@ExcelCol(column = Column.F, desc="物料号")
	private String materialcode;
	
	@ExcelCol(column = Column.G, desc="物料描述")
	private String materialName;
	
	@ExcelCol(column = Column.H, desc="单位")
	private String unitName;
	
	@ExcelCol(column = Column.I, desc="数量")
	private String orderQty;
	
	@ExcelCol(column = Column.J, desc="预计到货日期")
	private String rq;
	
	@ExcelCol(column = Column.K, desc="物流天数")
	private String ysts;
	
	@ExcelCol(column = Column.L, desc="供应商名称")
	private String vendorName;
	
	@ExcelCol(column = Column.M, desc="供应商编码")
	private String vendorCode;

	@ExcelCol(column = Column.N, desc="发布状态")
	private String publishStatus;
	
	@ExcelCol(column = Column.O, desc="确认状态")
	private String confirmStatus;


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getYsts() {
		return ysts;
	}

	public void setYsts(String ysts) {
		this.ysts = ysts;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getPublishStatus() {
		String status = "";
		if(publishStatus.equals("0")){
			status = "待发布";
		}else if(publishStatus.equals("1")){
			status = "已发布";
		}else if(publishStatus.equals("2")){
			status = "部分发布";
		}else{
			status = "";
		}
		return status;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getConfirmStatus() {
		String status = "";
		if(confirmStatus.equals("0")){
			status = "待确认";
		}else if(confirmStatus.equals("1")){
			status = "已确认";
		}else if(confirmStatus.equals("2")){
			status = "部分确认";
		}else if(confirmStatus.equals("-1")){
			status = "驳回";
		}else if(confirmStatus.equals("-2")){
			status = "拒绝驳回";
		}else{
			status = "";
		}
		return status;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getMaterialcode() {
		return materialcode;
	}

	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	
	

}
