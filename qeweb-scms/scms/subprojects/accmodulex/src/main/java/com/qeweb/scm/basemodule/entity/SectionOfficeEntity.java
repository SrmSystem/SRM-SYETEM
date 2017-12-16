package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 科室
 * @author SXL
 *
 */
@Entity
@Table(name="qeweb_section_office")
public class SectionOfficeEntity extends BaseEntity{

	private Long did;//部门ID
	
	private String code;//科室编号
	
	private String name;//科室名称
	
	private String remark;//备注

	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
