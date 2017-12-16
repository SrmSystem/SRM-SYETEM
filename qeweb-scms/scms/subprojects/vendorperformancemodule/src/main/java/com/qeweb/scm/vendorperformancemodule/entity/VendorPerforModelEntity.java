package com.qeweb.scm.vendorperformancemodule.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商绩效模型
 * @author pjjxiajun
 * @date 2015年10月22日
 * @path com.qeweb.scm.vendorperformancemodule.entity.VendorPerforModelEntity.java
 */
@Entity
@Table(name="qeweb_assess_model")
public class VendorPerforModelEntity extends BaseEntity{
	
	private String code;//编号
	
	private String name;//名称

	private Integer enableStatus;//允许状态
	
	/** 非数据库数据 */
	private Map<String,String> dimMapper;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	@Transient
	public Map<String, String> getDimMapper() {
		return dimMapper;
	}

	public void setDimMapper(Map<String, String> dimMapper) {
		this.dimMapper = dimMapper;
	}
	
	
	
	

}
