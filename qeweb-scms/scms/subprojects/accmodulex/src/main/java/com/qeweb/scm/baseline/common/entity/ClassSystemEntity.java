package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 班制
 *
 */
@Entity
@Table(name = "QEWEB_CLASS_SYSTEM")
public class ClassSystemEntity extends BaseEntity {
	private String name;//名称
	
	private Integer billType;//类型  5天1   5天半2   6天3
	
	private String remarks;
	
	private Integer month;
	
	private Double morningStart;//上午段开始
	
	private Double morningEnd;//上午段结束

	private Double afterStart;//下午段开始
	
	private Double afterEnd;//下午段结束
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Double getMorningStart() {
		return morningStart;
	}

	public void setMorningStart(Double morningStart) {
		this.morningStart = morningStart;
	}

	public Double getMorningEnd() {
		return morningEnd;
	}

	public void setMorningEnd(Double morningEnd) {
		this.morningEnd = morningEnd;
	}

	public Double getAfterStart() {
		return afterStart;
	}

	public void setAfterStart(Double afterStart) {
		this.afterStart = afterStart;
	}

	public Double getAfterEnd() {
		return afterEnd;
	}

	public void setAfterEnd(Double afterEnd) {
		this.afterEnd = afterEnd;
	}
	
	
}
