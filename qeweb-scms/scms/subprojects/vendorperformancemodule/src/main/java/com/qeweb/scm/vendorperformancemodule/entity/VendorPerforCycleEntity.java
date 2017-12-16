package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 周期设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_cycle")
public class VendorPerforCycleEntity  extends BaseEntity{
	
	private String code;	
	private String cycleName;	
	private Integer initDates;//准备天数
	private Integer fixDates;//维度得分调整天数
	private Integer defaultPurchase;
	private String remarks;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	public Integer getInitDates() {
		return initDates;
	}
	public void setInitDates(Integer initDates) {
		this.initDates = initDates;
	}
	public Integer getFixDates() {
		return fixDates;
	}
	public void setFixDates(Integer fixDates) {
		this.fixDates = fixDates;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getDefaultPurchase() {
		return defaultPurchase;
	}
	public void setDefaultPurchase(Integer defaultPurchase) {
		this.defaultPurchase = defaultPurchase;
	}
}
