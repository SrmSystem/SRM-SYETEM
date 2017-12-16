package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 主供事业部
 * @author Administrator
 *
 */
@Entity
@Table(name = "QEWEB_VENDOR_BU_CFG")
public class VendorBUEntity extends IdEntity{

	private String codes;
	
	private String name;

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
