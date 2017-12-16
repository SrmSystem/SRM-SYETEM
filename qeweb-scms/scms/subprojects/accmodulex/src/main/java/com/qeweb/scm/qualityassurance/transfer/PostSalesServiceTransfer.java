package com.qeweb.scm.qualityassurance.transfer;

import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "售后质量信息")
public class PostSalesServiceTransfer {
	
	@ExcelCol(column = Column.A)   
	private String startTime;
	
	@ExcelCol(type =ColType.DATE, column = Column.B, required = true)   
	private String endTime;//结束时间
	
	@ExcelCol(type =ColType.DATE, column = Column.C, required = true)   
	private String repairTime;//维修时间
	
	@ExcelCol(column = Column.D, required = true)   
	private String vendorCode;
	
	@ExcelCol(column = Column.E)   
	private String vendorName;
	
	@ExcelCol(column = Column.F, required = true)   
	private String materialCode;
	
	@ExcelCol(column = Column.G)   
	private String materialName;
	
	@ExcelCol(column = Column.H, required = true)   
	private String generatings;//生成厂
	
	@ExcelCol(column = Column.I, required = true)  
	private String hosts;//主机厂
	
	@ExcelCol(column = Column.J, required = true)  
	private String models;//车型
	
	@ExcelCol(column = Column.K, required = true)   
	private String totalNumber;//总数
	
	@ExcelCol(column = Column.L, required = true)   
	private String totalCounts;//总数
	
	@ExcelCol(column = Column.M, required = true)   
	private String describe;//标准故障

	@ExcelCol(column = Column.N, required = true)   
	private String driving;//行程里数
	
	@ExcelCol(column = Column.O, required = true)   
	private String agreement;//协议号
	
	@ExcelCol(column = Column.P, required = true)   
	private String totalmodel;//总成型号
	
	@ExcelCol(column = Column.Q, required = true)   
	private String outcode;//出厂编号
	
	@ExcelCol(column = Column.R, required = true)   
	private String area;//片区
	
	@ExcelCol(column = Column.S, required = true)
	private String station;//服务站
	
	@ExcelCol(column = Column.T, required = true)
	private String fault;//故障简述
	
	@ExcelCol(column = Column.U, required = true)
	private String reason;//故障原因与分析

	public String getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}

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

	public String getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(String repairTime) {
		this.repairTime = repairTime;
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

	public String getGeneratings() {
		return generatings;
	}

	public void setGeneratings(String generatings) {
		this.generatings = generatings;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDriving() {
		return driving;
	}

	public void setDriving(String driving) {
		this.driving = driving;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getTotalmodel() {
		return totalmodel;
	}

	public void setTotalmodel(String totalmodel) {
		this.totalmodel = totalmodel;
	}

	public String getOutcode() {
		return outcode;
	}

	public void setOutcode(String outcode) {
		this.outcode = outcode;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
