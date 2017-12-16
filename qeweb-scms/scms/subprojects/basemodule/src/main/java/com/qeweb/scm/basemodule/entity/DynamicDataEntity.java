package com.qeweb.scm.basemodule.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "qeweb_dynamicdata")
public class DynamicDataEntity extends BaseEntity {

	private String beanId; 		// beanID
	private String objectName; 	// 对象名称
	private String remark; 		// 备注
	private Integer enable;		// 启用 0：禁用 1:启用
	private Set<DynamicDataSceneEntity> colSceneItem;

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@OneToMany(mappedBy="dataEx")
	@JsonIgnore
	public Set<DynamicDataSceneEntity> getColSceneItem() {
		return colSceneItem;
	}

	public void setColSceneItem(Set<DynamicDataSceneEntity> colSceneItem) {
		this.colSceneItem = colSceneItem;
	}

}
