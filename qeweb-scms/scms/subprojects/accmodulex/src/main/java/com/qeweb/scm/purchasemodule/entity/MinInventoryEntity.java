package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;

/**
 * 最小库存设置
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_min_inventory")
public class MinInventoryEntity extends BaseEntity {

	private OrganizationEntity vendor;
	private MaterialEntity material;
	private Double stockMinQty;
	private String remark;

	@ManyToOne
	@JoinColumn(name="vendor_id")
	public OrganizationEntity getVendor() {
		return vendor;
	}

	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name="material_id")
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	@Column(name="stock_min_qty")
	public Double getStockMinQty() {
		return stockMinQty;
	}

	public void setStockMinQty(Double stockMinQty) {
		this.stockMinQty = stockMinQty;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
