package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 工厂对应采购组
 * @author chao.gu
 * @date 2017年5月11日
 */
@Entity
@Table(name="QEWEB_GROUP_FACTORY")
public class GroupFactoryRelEntity extends IdEntity {
    /**
     * 工厂的id
     */
	private Long factoryId;
	
	/**
	 * 采购组id
	 */
	private Long groupId;
	
    /**
     * 描述
     */
	private String remark;
	
	/**
     * 废除
     */
	private Integer abolished;
	
	private FactoryEntity factory;
	private PurchasingGroupEntity group;
	
	//非持久化字段
	private String factoryCode;
	private String factoryName;
	private String groupCode;
	private String groupName;
	
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	@ManyToOne
	@JoinColumn(name="factoryId",insertable=false,updatable=false)
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@ManyToOne
	@JoinColumn(name="groupId",insertable=false,updatable=false)
	public PurchasingGroupEntity getGroup() {
		return group;
	}
	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	@Transient
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	@Transient
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Transient
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	@Transient
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public Integer getAbolished() {
		return abolished;
	}
	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}
	
	
}
