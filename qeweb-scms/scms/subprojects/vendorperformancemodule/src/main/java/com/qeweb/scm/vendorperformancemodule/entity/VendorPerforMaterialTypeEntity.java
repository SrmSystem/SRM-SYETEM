package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 物料类别管理
 * @author sxl
 *
 */
@Entity
@Table(name="QEWEB_ASSESS_MATERIAL_TYPE")
public class VendorPerforMaterialTypeEntity  extends BaseEntity{
	
	private String code;//编码	
	private String name;//名称	
	private String remarks;//备注
	
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
