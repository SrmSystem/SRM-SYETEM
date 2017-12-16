package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 公式配置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_f_allocation")
public class VendorPerforFAllocationEntity  extends IdEntity{
	
	private String name;	
	private String describe;
	private String fallValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getFallValue() {
		return fallValue;
	}
	public void setFallValue(String fallValue) {
		this.fallValue = fallValue;
	}	
}
