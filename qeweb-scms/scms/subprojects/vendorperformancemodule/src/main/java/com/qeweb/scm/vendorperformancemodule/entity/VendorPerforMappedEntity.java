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
@Table(name="qeweb_assess_mapped")
public class VendorPerforMappedEntity  extends IdEntity{
	
	private String name;//名称	
	
	private String describe;//描述
	
	private Integer mappedType;//类型
	
	private String mappedValue;//值 1：SQL,2:数值
	
	private String mappedName;//映射名称
	
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
	public Integer getMappedType() {
		return mappedType;
	}
	public void setMappedType(Integer mappedType) {
		this.mappedType = mappedType;
	}
	public String getMappedValue() {
		return mappedValue;
	}
	public void setMappedValue(String mappedValue) {
		this.mappedValue = mappedValue;
	}
	public String getMappedName() {
		return mappedName;
	}
	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}
}
