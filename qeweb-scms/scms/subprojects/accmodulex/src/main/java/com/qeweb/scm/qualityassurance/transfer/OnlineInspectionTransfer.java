package com.qeweb.scm.qualityassurance.transfer;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "入厂检验不合格信息")
public class OnlineInspectionTransfer {
	
	@ExcelCol(type =ColType.DATE, column = Column.A, required = true, desc="时间")   
	private String startTime;
	
	@ExcelCol(column = Column.B, required = true, desc="供应商编码")   
	private String vendorCode;
	
	@ExcelCol(column = Column.C, desc="供应商名称")   
	private String vendorName;
	
	@ExcelCol(column = Column.D, required = true, desc="物料编码")   
	private String materialCode;
	
	@ExcelCol(column = Column.E, desc="物料名称")   
	private String materialName;
	
	@ExcelCol(column = Column.F, required = true, desc="总数")   
	private String totalNumber;//总数
	
	@ExcelCol(column = Column.G, required = true, desc="考核金额")   
	private String money;//考核金额

	@ExcelCol(column = Column.H, required = true, desc="发生地点")   
	private String stages;//发生地点

	@ExcelCol(column = Column.I, desc="描述")   
	private String describe;//描述

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
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

	public String getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getStages() {
		return stages;
	}

	public void setStages(String stages) {
		this.stages = stages;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
