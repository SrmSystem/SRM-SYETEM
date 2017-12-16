package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公司的相关信息
 * @author eleven
 * @date 2017年5月10日
 * @path com.qeweb.scm.basemodule.entity.CompanyEntity.java
 */
@Entity
@Table(name="qeweb_company")
public class CompanyEntity extends BaseEntity {

    /**
     * 公司名称
     */
	private String name;
	
    /**
     * 公司代码
     */
	private String code;
	
	/**
     * 公司注释
     */
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
