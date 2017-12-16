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

@Entity
@Table(name = "qeweb_qm_postsalesservice")
public class PostSalesServiceEntity extends BaseEntity{

	private Timestamp startTime;//开始时间
	
	private Timestamp endTime;//结束时间
	
	private String month; //月份
	
	private Timestamp repairTime;//维修时间
	
	private String code;//编号
	
	private Long vendorId;//供应商ID
	
	private Long materialId;//物料ID
	
	private String generatings;//生成厂
	
	private String hosts;//生成厂
	
	private String models;//车型
	
	private Integer totalNumber;//台数
	
	private String describe;//标准故障
	
	private String driving;//行程里数
	
	private String agreement;//协议号
	
	private String totalmodel;//总成型号
	
	private String outcode;//出厂编号
	
	private String area;//片区
	
	private String station;//服务站
	
	private String fault;//故障简述
	
	private String reason;//故障原因与分析
	
	private Integer qualityStatus;//状态
	
	private String salePpm;    //售后PPM
	
	private Integer totalCounts;	//售后总数
	
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	
	private MaterialEntity materialEntity;
	
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
	public MaterialEntity getMaterialEntity() {
		return materialEntity;
	}

	public void setMaterialEntity(MaterialEntity materialEntity) {
		this.materialEntity = materialEntity;
	}
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Timestamp repairTime) {
		this.repairTime = repairTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
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

	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}

	public String getSalePpm() {
		return salePpm;
	}

	public void setSalePpm(String salePpm) {
		this.salePpm = salePpm;
	}

	public Integer getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}
	
}
