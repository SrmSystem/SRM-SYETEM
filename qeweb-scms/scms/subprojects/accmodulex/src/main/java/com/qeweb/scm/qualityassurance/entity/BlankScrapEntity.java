package com.qeweb.scm.qualityassurance.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 *毛坯废品信息
 * @author xs
 *
 */
@Entity
@Table(name = "qeweb_qm_blank_scrap")
public class BlankScrapEntity  extends BaseEntity{
	
	private Timestamp startTime;	//开始时间
	private Timestamp endTime;	//结束时间
	private String month; // 月份
	private String 	manufacturerCode;	//	生产厂家编码
	private String  manufacturer;	//	生产厂家
	private String 	drawingNo;	//图号
	private String 	partsName;	//零件名称
	private Integer amount;	//不合格数量（件）	
	private Integer totalAmount;	//总数量（件）	
	private Integer unqualifiedAmount;	//不合格数量（件）
	private String 	unqualifiedPhenomenon;	//不合格现象描述
	private String 	handle;	//处置		
	private Integer state;	//状态	
	private Integer total;	//供货总数量	
	private Double 	rejectionRate;	//毛坯废品率
	
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@Column(name="total_amount")
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Double getRejectionRate() {
		return rejectionRate;
	}
	public void setRejectionRate(Double rejectionRate) {
		this.rejectionRate = rejectionRate;
	}
	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}
	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}
	
	

}
