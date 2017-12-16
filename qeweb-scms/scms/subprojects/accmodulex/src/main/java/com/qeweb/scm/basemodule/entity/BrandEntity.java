package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_brand")
public class BrandEntity extends BaseEntity {
	private String name;
	private String code;
	private String remark;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
