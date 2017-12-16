package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 
 * @author pjjxiajun
 * @date 2015年4月1日
 * @path com.qeweb.scm.rbac.entity.DataRightEntity.java
 */
//@Entity
//@Table(name = "qeweb_data_permission")
public class DataPermissionEntity extends BaseEntity{
	private String domainName;
	private String domainClass;
	private String userIds;
	private String userField;
	private String orgIds;
	private String orgField;
	private String fields;
	private String operations;
	private Integer onlyOwner;
	private String customerScript;
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(String domainClass) {
		this.domainClass = domainClass;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUserField() {
		return userField;
	}
	public void setUserField(String userField) {
		this.userField = userField;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	public String getOrgField() {
		return orgField;
	}
	public void setOrgField(String orgField) {
		this.orgField = orgField;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getOperations() {
		return operations;
	}
	public void setOperations(String operations) {
		this.operations = operations;
	}
	public Integer getOnlyOwner() {
		return onlyOwner;
	}
	public void setOnlyOwner(Integer onlyOwner) {
		this.onlyOwner = onlyOwner;
	}
	public String getCustomerScript() {
		return customerScript;
	}
	public void setCustomerScript(String customerScript) {
		this.customerScript = customerScript;
	}
	
	
}
