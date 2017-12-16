package com.qeweb.scm.qualityassurance.transfer;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "入厂检验不合格信息")
public class InFactoryUnqualifiedTransfer {
	
	@ExcelCol(column = Column.A)   
	private String startTime;
	
	@ExcelCol(type =ColType.DATE, column = Column.B, required = true)   
	private String endTime;//结束时间
	
	@ExcelCol(column = Column.C, required = true)   
	private String vendorCode;
	
	@ExcelCol(column = Column.D)   
	private String vendorName;
	
	@ExcelCol(column = Column.E, required = true)   
	private String materialCode;
	
	@ExcelCol(column = Column.F)   
	private String materialName;
	
	@ExcelCol(column = Column.G, required = true)   
	private String totalNumber;//总数
	
	@ExcelCol(column = Column.H, required = true)   
	private String unqualified;//不合格

	@ExcelCol(column = Column.I, required = true)   
	private String sampling;//抽检数

	@ExcelCol(column = Column.J, required = true)   
	private String dispose;//处置
	
	@ExcelCol(column = Column.K)   
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

	public String getUnqualified() {
		return unqualified;
	}

	public void setUnqualified(String unqualified) {
		this.unqualified = unqualified;
	}

	public String getSampling() {
		return sampling;
	}

	public void setSampling(String sampling) {
		this.sampling = sampling;
	}

	public String getDispose() {
		return dispose;
	}

	public void setDispose(String dispose) {
		this.dispose = dispose;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
