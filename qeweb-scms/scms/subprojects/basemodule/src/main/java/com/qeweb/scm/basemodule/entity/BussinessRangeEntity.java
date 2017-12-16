package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * 公司业务范围，同时包含公司业务类型
 * @author pjjxiajun
 * @date 2015年6月17日
 * @path com.qeweb.scm.basemodule.entity.BussinessRangeEntity.java
 */
@Entity
@Table(name="qeweb_bussiness_range")
public class BussinessRangeEntity extends BaseEntity {
	private String name;
	private String code;
	private String remark;
	private Integer bussinessType;//0:业务范围>1：业务类型>2:品牌>3:产品线
	private Long parentId;//业务范围为0,业务类型为为业务范围的子集
	
	
	/** 非持久化 */
	private BussinessRangeEntity range;//业务类型关联业务范围
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
	public Integer getBussinessType() {
		return bussinessType;
	}
	public void setBussinessType(Integer bussinessType) {
		this.bussinessType = bussinessType;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@ManyToOne(optional=true)
	@JoinColumn(name="parentId",insertable=false,updatable=false)
	public BussinessRangeEntity getRange() {
		return range;
	}
	public void setRange(BussinessRangeEntity range) {
		this.range = range;
	}
	
	
	
	
	
}
