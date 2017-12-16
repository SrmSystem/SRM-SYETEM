package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="qeweb_company_bussiness")
public class CompanyBussinessEntity extends BaseEntity {
	private String name;
	private String code;
	private Long bussinessRangeId;
	private String bussinessRangeCode;
	private String bussinessRangeName;
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
	public Long getBussinessRangeId() {
		return bussinessRangeId;
	}
	public void setBussinessRangeId(Long bussinessRangeId) {
		this.bussinessRangeId = bussinessRangeId;
	}
	public String getBussinessRangeCode() {
		return bussinessRangeCode;
	}
	public void setBussinessRangeCode(String bussinessRangeCode) {
		this.bussinessRangeCode = bussinessRangeCode;
	}
	public String getBussinessRangeName() {
		return bussinessRangeName;
	}
	public void setBussinessRangeName(String bussinessRangeName) {
		this.bussinessRangeName = bussinessRangeName;
	}
	
	
	
}
