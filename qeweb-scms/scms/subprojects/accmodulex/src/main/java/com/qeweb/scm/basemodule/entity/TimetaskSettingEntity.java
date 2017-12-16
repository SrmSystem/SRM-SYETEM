package com.qeweb.scm.basemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;



/**
 * 总得分值对象
 * @author qingmiao
 * @date 2016年1月10日
 * @path com.qeweb.scm.basemodule.entity.TimetaskSettingEntity.java
 */
@Entity
@Table(name="QEWEB_TIMETASK_SETTING")
public class TimetaskSettingEntity extends BaseEntity{
	
	private String day; //天
	
	private String month;//月
	
	private String taskName; //任务名称
	
	private Long vendorId; //供应商id
	
	private OrganizationEntity vendor; //供应商

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@ManyToOne
	@JoinColumn(name="VENDOR_ID",insertable=false,updatable=false)
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@Column(name="VENDOR_ID")
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
}
