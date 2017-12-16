package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 采购额导入
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_purchasedatain")
public class VendorPerforPurchasedatainEntity  extends BaseEntity{
	
	private String vendorCode;//供应商Code	
	
	private String vendorName;//供应商名称	
	
	private Long brandId;	
	private String brandName;//品牌名称
	
	private String vendorDate;//供货月份
	
	private Integer defaultPurchase;//采购额

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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getVendorDate() {
		return vendorDate;
	}

	public void setVendorDate(String vendorDate) {
		this.vendorDate = vendorDate;
	}

	public Integer getDefaultPurchase() {
		return defaultPurchase;
	}

	public void setDefaultPurchase(Integer defaultPurchase) {
		this.defaultPurchase = defaultPurchase;
	}
}
