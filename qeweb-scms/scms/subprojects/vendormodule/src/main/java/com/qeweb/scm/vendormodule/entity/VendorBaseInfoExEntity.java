package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 供应商基本信息的扩展分为普通字段和列表
 * @author pjjxiajun
 * @date 2015年6月24日
 * @path com.qeweb.scm.vendormodule.entity.VendorBaseInfoExEntity.java
 */
@Entity
@Table(name = "QEWEB_VENDOR_BASE_INFO_EX")
public class VendorBaseInfoExEntity extends IdEntity{
	private Long vendorId;
	private Long orgId;
	private Integer exType;
	private String motorFactory;//主机厂
	private String productLine;//产品线
	private String carModel;//车型
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Integer getExType() {
		return exType;
	}
	public void setExType(Integer exType) {
		this.exType = exType;
	}
	public String getMotorFactory() {
		return motorFactory;
	}
	public void setMotorFactory(String motorFactory) {
		this.motorFactory = motorFactory;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	
	

}
