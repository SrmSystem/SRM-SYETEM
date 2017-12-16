package com.qeweb.scm.epmodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.EPBaseEntity;



@Entity
@Table(name = "QEWEB_EP_QUO_SUB_ITEM")
public class EpQuoSubItemEntity extends EPBaseEntity {
	
	private Long epMaterialId;
	private Long parentId; // 上级明细id
	private String name; // 报价类型名称
	private String unitId; // 单位
	private String remarks; // 备注
	private Integer isTop; // 是否一级明细
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getIsTop() {
		return isTop;
	}
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	public Long getEpMaterialId() {
		return epMaterialId;
	}
	public void setEpMaterialId(Long epMaterialId) {
		this.epMaterialId = epMaterialId;
	}

	



}
