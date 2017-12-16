package com.qeweb.scm.qualityassurance.transfer;


import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "毛坯废品信息")
public class BlankScrapTransfer {
	
	@ExcelCol(column = Column.A, desc="开始时间")   
	private String startTime;
	
	@ExcelCol(type =ColType.DATE, column = Column.B, required = true, desc="结束时间")   
	private String endTime;
	
	@ExcelCol(column = Column.C, required = true, desc="生产厂家编码")   
	private String manufacturerCode;
	
	@ExcelCol(column = Column.D, required = true, desc="生产厂家")   
	private String manufacturer;
	
	@ExcelCol(column = Column.E, required = true, desc="图号")   
	private String drawingNo;
	
	@ExcelCol(column = Column.F, required = true, desc="零件名称")   
	private String partsName;
	
	@ExcelCol(column = Column.G, required = true, desc="数量")   
	private String amount;
	
	@ExcelCol(column = Column.H, required = true, desc="不合格现象描述")   
	private String unqualifiedPhenomenon;

	@ExcelCol(column = Column.I, required = true, desc="处置方式")   
	private String handle;

	@ExcelCol(column = Column.J, desc="状态")   
	private String state;
	
	@ExcelCol(column = Column.K, required = true, desc="总数量")   
	private String totalAmount;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDrawingNo() {
		return drawingNo;
	}

	public void setDrawingNo(String drawingNo) {
		this.drawingNo = drawingNo;
	}

	public String getPartsName() {
		return partsName;
	}

	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUnqualifiedPhenomenon() {
		return unqualifiedPhenomenon;
	}

	public void setUnqualifiedPhenomenon(String unqualifiedPhenomenon) {
		this.unqualifiedPhenomenon = unqualifiedPhenomenon;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
