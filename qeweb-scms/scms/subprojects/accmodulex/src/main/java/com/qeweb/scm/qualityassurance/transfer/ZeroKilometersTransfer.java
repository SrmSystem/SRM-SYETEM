package com.qeweb.scm.qualityassurance.transfer;


import com.qeweb.scm.basemodule.annotation.ColType;
import com.qeweb.scm.basemodule.annotation.Column;
import com.qeweb.scm.basemodule.annotation.ExcelCol;
import com.qeweb.scm.basemodule.annotation.ExcelTransfer;

@ExcelTransfer(start=1, describe = "零公里质检")
public class ZeroKilometersTransfer {
	@ExcelCol(column = Column.A)   
	private String startTime;
	
	@ExcelCol(type =ColType.DATE,column = Column.B)   
	private String endTime;
	
	@ExcelCol(column = Column.C)   
	private String reportCode;
	
	@ExcelCol(column = Column.D)   
	private String vendorCode;
	
	@ExcelCol(column = Column.E)   
	private String vendorName;
	
	@ExcelCol(column = Column.F)   
	private String firstPictureCode;
	
	@ExcelCol(column = Column.G)   
	private String firstPictureName;
	
	@ExcelCol(column = Column.H)   
	private String factory;
	
	@ExcelCol(column = Column.I)   
	private String motorFactory;
	
	@ExcelCol(column = Column.J)   
	private String models;
	
	@ExcelCol(column = Column.K)   
	private String standardFault;

	@ExcelCol(column = Column.L)   
	private String counts;
	
	@ExcelCol(column = Column.M)   
	private String totalCounts;

	@ExcelCol(column = Column.N)   
	private String mileage;

	@ExcelCol(column = Column.O)   
	private String agreementNo;

	@ExcelCol(column = Column.P)   
	private String assemblyModel ;

	@ExcelCol(column = Column.Q)   
	private String appearanceNumber ;

	@ExcelCol(column = Column.R)   
	private String area; 

	@ExcelCol(column = Column.S)   
	private String serviceStation; 

	@ExcelCol(type =ColType.DATE,column = Column.T)   
	private String maintenanceTime;

	@ExcelCol(column = Column.U)   
	private String faultDescription; 

	@ExcelCol(column = Column.V)   
	private String causeAndAnalysis;

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

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
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

	public String getFirstPictureCode() {
		return firstPictureCode;
	}

	public void setFirstPictureCode(String firstPictureCode) {
		this.firstPictureCode = firstPictureCode;
	}

	public String getFirstPictureName() {
		return firstPictureName;
	}

	public void setFirstPictureName(String firstPictureName) {
		this.firstPictureName = firstPictureName;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getMotorFactory() {
		return motorFactory;
	}

	public void setMotorFactory(String motorFactory) {
		this.motorFactory = motorFactory;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getStandardFault() {
		return standardFault;
	}

	public void setStandardFault(String standardFault) {
		this.standardFault = standardFault;
	}

	public String getCounts() {
		return counts;
	}

	public void setCounts(String counts) {
		this.counts = counts;
	}

	public String getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public String getAssemblyModel() {
		return assemblyModel;
	}

	public void setAssemblyModel(String assemblyModel) {
		this.assemblyModel = assemblyModel;
	}

	public String getAppearanceNumber() {
		return appearanceNumber;
	}

	public void setAppearanceNumber(String appearanceNumber) {
		this.appearanceNumber = appearanceNumber;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getServiceStation() {
		return serviceStation;
	}

	public void setServiceStation(String serviceStation) {
		this.serviceStation = serviceStation;
	}

	

	public String getMaintenanceTime() {
		return maintenanceTime;
	}

	public void setMaintenanceTime(String maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}

	public String getFaultDescription() {
		return faultDescription;
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	public String getCauseAndAnalysis() {
		return causeAndAnalysis;
	}

	public void setCauseAndAnalysis(String causeAndAnalysis) {
		this.causeAndAnalysis = causeAndAnalysis;
	}

	
}
