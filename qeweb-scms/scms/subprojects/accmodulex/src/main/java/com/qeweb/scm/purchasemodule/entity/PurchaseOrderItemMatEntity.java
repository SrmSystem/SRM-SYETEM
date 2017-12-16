package com.qeweb.scm.purchasemodule.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

@Entity
@Table(name = "QEWEB_ORDER_ITEM_MAT")
public class PurchaseOrderItemMatEntity extends BaseEntity {
	
	private PurchaseOrderItemEntity orderItem;
	
	private Long materialId;//组件物料
	
	private MaterialEntity material;
	
	private String itemNo;
	
	private String unit;
	
	private Double orderNum;
	
	private Timestamp orderTime;
	
	//冗余
	private Double orderCount;
	private String tableDatas;
	private String materialCode;

	@ManyToOne
	@JoinColumn(name="order_item_id")
	public PurchaseOrderItemEntity getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(PurchaseOrderItemEntity orderItem) {
		this.orderItem = orderItem;
	}

	public Double getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	@Transient
	public Double getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Double orderCount) {
		this.orderCount = orderCount;
	}

	@Transient
	public String getTableDatas() {
		return tableDatas;
	}

	public void setTableDatas(String tableDatas) {
		this.tableDatas = tableDatas;
	}
	
	
	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	
	@Column(name="material_id")
	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	
	@ManyToOne
	@JoinColumn(name = "material_id",insertable = false,updatable=false)
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	
	
	

	

}
