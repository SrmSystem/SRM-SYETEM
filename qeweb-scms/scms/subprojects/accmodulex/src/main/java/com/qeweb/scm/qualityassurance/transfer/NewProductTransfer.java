package com.qeweb.scm.qualityassurance.transfer;

import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "新产品开发质量")
public class NewProductTransfer {
	@ExcelCol(column = Column.A, required = true)   
	private String seq;
	
	@ExcelCol(column = Column.B, required = true)   
	private String qmTime;
	
	@ExcelCol(column = Column.C, required = true)   
	private String vendorCode;
	
	@ExcelCol(column = Column.D)   
	private String vendorName;
	
	@ExcelCol(column = Column.E, required = true)   
	private String matName;
	
	@ExcelCol(column = Column.F, required = true)   
	private String sampleSize;
	
	@ExcelCol(column = Column.G, required = true)   
	private String deliverTimes;
	
	@ExcelCol(column = Column.H, required = true)   
	private String qualified;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getQmTime() {
		return qmTime;
	}

	public void setQmTime(String qmTime) {
		this.qmTime = qmTime;
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

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(String sampleSize) {
		this.sampleSize = sampleSize;
	}

	public String getDeliverTimes() {
		return deliverTimes;
	}

	public void setDeliverTimes(String deliverTimes) {
		this.deliverTimes = deliverTimes;
	}

	public String getQualified() {
		return qualified;
	}

	public void setQualified(String qualified) {
		this.qualified = qualified;
	}
}
