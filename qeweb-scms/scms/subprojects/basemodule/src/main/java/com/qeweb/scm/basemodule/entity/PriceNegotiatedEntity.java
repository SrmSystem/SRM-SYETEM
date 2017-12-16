package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 供货商协议价
 *
 */
@Entity
@Table(name="qeweb_price_negotiated")
public class PriceNegotiatedEntity extends IdEntity{

	private String vendorCode;//供应商Code	
	
	private String vendorName;//供应商名称	
	
	private String materialCode;//物料COde
	
	private String materialName;//物理名称
	
	private Double negotiatedPrices;//协议价格
	
	private String negotiatedUnit;//协议单位
	
	private String negotiatedCurrency;//协议币种
	
	private Integer currentVersion;//是否是当前版本 1
	
	private Timestamp negotiatedStartDate;
	
	private Timestamp negotiatedEndDate;

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

	public Double getNegotiatedPrices() {
		return negotiatedPrices;
	}

	public void setNegotiatedPrices(Double negotiatedPrices) {
		this.negotiatedPrices = negotiatedPrices;
	}

	public String getNegotiatedUnit() {
		return negotiatedUnit;
	}

	public void setNegotiatedUnit(String negotiatedUnit) {
		this.negotiatedUnit = negotiatedUnit;
	}
	
	public String getNegotiatedCurrency() {
		return negotiatedCurrency;
	}

	public Integer getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(Integer currentVersion) {
		this.currentVersion = currentVersion;
	}
	
	public void setNegotiatedCurrency(String negotiatedCurrency) {
		this.negotiatedCurrency = negotiatedCurrency;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getNegotiatedStartDate() {
		return negotiatedStartDate;
	}

	public void setNegotiatedStartDate(Timestamp negotiatedStartDate) {
		this.negotiatedStartDate = negotiatedStartDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getNegotiatedEndDate() {
		return negotiatedEndDate;
	}

	public void setNegotiatedEndDate(Timestamp negotiatedEndDate) {
		this.negotiatedEndDate = negotiatedEndDate;
	}
}
