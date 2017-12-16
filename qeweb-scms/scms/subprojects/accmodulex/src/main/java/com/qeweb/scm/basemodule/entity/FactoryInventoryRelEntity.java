package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 工厂和库存地点的关系表
 * @author eleven
 * @date 2017年5月17日
 * @path com.qeweb.scm.basemodule.entity.FactoryInventoryRelEntity.java
 */
@Entity
@Table(name="qeweb_factory_inventory")
public class FactoryInventoryRelEntity extends IdEntity {
	
    /**
     * 工厂的id
     */
	private Long factoryId;
	
    /**
     * 库存地点的id
     */
	private Long inventoryId;
	
	/**
     * 描述
     */
	private String remark;
	
	
	/**
     * 废除
     */
	private Integer abolished;
	
	/** 非持久化字段 */
	private InventoryLocationEntity inventory;
	private FactoryEntity factory;
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@JoinColumn(name="inventoryId",insertable=false,updatable=false)
	public InventoryLocationEntity getInventory() {
		return inventory;
	}
	public void setInventory(InventoryLocationEntity inventory) {
		this.inventory = inventory;
	}
	
	@ManyToOne
	@JoinColumn(name="factoryId",insertable=false,updatable=false)
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}
	
	
	
	
	
}
