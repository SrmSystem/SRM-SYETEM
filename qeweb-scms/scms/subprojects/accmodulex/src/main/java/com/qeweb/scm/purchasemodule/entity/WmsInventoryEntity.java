package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * WMS库存量
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_wms_inventory")
public class WmsInventoryEntity extends BaseEntity {

	private MaterialEntity material;			// 物料
	private Double stockQty;					// 库存量
	private Double reqMonthQty;					// 当月需求量
	
	//非持久化
	private String materialCode;
	private String materialName;

	@ManyToOne
	@JoinColumn(name="material_id")	
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="stock_qty")
	public Double getStockQty() {
		return stockQty;
	}

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	
	@Column(name="req_month_qty")
	public Double getReqMonthQty() {
		return reqMonthQty;
	}

	public void setReqMonthQty(Double reqMonthQty) {
		this.reqMonthQty = reqMonthQty;
	}

	@Transient
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@Transient
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

}
