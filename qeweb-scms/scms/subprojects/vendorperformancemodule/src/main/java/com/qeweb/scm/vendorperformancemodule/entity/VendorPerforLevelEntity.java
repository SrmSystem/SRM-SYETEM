package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 等级设置
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_level")
public class VendorPerforLevelEntity  extends BaseEntity{
	
	private String code;	
	private String levelName;	
	private Integer lowerLimit;//等级下限
	private Integer upperLimit;//等级上限
	private Integer quadrant;//象限
	private String remarks;
	private long fatherId;//上层ID
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public Integer getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(Integer upperLimit) {
		this.upperLimit = upperLimit;
	}
	public Integer getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public Integer getQuadrant() {
		return quadrant;
	}
	public void setQuadrant(Integer quadrant) {
		this.quadrant = quadrant;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public long getFatherId() {
		return fatherId;
	}
	public void setFatherId(long fatherId) {
		this.fatherId = fatherId;
	}
}
