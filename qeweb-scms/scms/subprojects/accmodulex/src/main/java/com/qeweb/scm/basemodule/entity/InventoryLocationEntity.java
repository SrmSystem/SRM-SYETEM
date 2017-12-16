package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 库存地点
 * @author eleven
 * @date 2017年5月10日
 * @path com.qeweb.scm.basemodule.entity.InventoryLocationEntity.java
 */
@Entity
@Table(name="QEWEB_INVENTORY_LOCATION")
public class InventoryLocationEntity extends BaseEntity {

    /**
     * 库存名称
     */
	private String name;
	
    /**
     * 库存代码
     */
	private String code;
	
    /**
     * 库存详细地址
     */
	private String address;
	
	/**
     * 库存注释
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
