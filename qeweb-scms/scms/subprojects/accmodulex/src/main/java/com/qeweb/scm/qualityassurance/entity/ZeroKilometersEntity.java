package com.qeweb.scm.qualityassurance.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;

/**
 * 零公里
 * @author xs
 *
 */
@Entity
@Table(name = "qeweb_qm_zero_kilometers")
public class ZeroKilometersEntity extends BaseEntity{
	private Timestamp startTime; //开始时间
	private Timestamp endTime; //结束时间
	private String month;  //yufen 
	private Timestamp maintenanceTime; //维修时间	
	private String reportCode;  //报告单编码
	private Long vendorId;	//供应商ID
	private Long materialId;//物料ID
	private String factory;	//生产厂
	private String motorFactory;	//主机厂
	private String models;	//车型
	private Integer counts;	//台数
	private String standardFault;    //标准故障
	private Integer mileage;	//行驶里程
	private String agreementNo;    //协议号
	private String assemblyModel ;    //总成型号
	private String appearanceNumber ;    //出场编号
	private String area;    //片区
	private String serviceStation;    //服务站
	private String faultDescription;    //故障简述
	private String causeAndAnalysis;    //故障原因与分析
	private Integer status;	//0未发布,1发布,-1关闭
	private String zeroKilometersPpm;    //零公里PPM
	private Integer totalCounts;	//入库总数
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	private MaterialEntity material; // 物料
//	private String vendorCode;	//供应商代码
//	private String vendorName;	//供应商名称
//	private String firstPictureCode;	//祸首件图号
//	private String firstPictureName;	//祸首件名称
	private String qualityRequest;	//质量部要求
	
	public String getQualityRequest() {
		return qualityRequest;
	}
	public void setQualityRequest(String qualityRequest) {
		this.qualityRequest = qualityRequest;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}
	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}
	@ManyToOne
	@JoinColumn(name = "materialId",insertable = false,updatable = false)
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
//	public String getVendorCode() {
//		return vendorCode;
//	}
//	public void setVendorCode(String vendorCode) {
//		this.vendorCode = vendorCode;
//	}
//	public String getVendorName() {
//		return vendorName;
//	}
//	public void setVendorName(String vendorName) {
//		this.vendorName = vendorName;
//	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
//	public String getFirstPictureCode() {
//		return firstPictureCode;
//	}
//	public void setFirstPictureCode(String firstPictureCode) {
//		this.firstPictureCode = firstPictureCode;
//	}
//	public String getFirstPictureName() {
//		return firstPictureName;
//	}
//	public void setFirstPictureName(String firstPictureName) {
//		this.firstPictureName = firstPictureName;
//	}
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

	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public Integer getMileage() {
		return mileage;
	}
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getMaintenanceTime() {
		return maintenanceTime;
	}
	public void setMaintenanceTime(Timestamp maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}

	public String getStandardFault() {
		return standardFault;
	}
	public void setStandardFault(String standardFault) {
		this.standardFault = standardFault;
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
	public Integer getTotalCounts() {
		return totalCounts;
	}
	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}
	public String getZeroKilometersPpm() {
		return zeroKilometersPpm;
	}
	public void setZeroKilometersPpm(String zeroKilometersPpm) {
		this.zeroKilometersPpm = zeroKilometersPpm;
	}
	
	
	
	
	
	

}
