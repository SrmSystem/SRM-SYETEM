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
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.StoreHouseEntity;

/**
 * VMI库存量
 * 
 * @author ALEX
 *
 */
@Entity
@Table(name = "qeweb_inventory")
public class InventoryEntity extends BaseEntity {

	private StoreHouseEntity store;				// 仓库
	private MinInventoryEntity minInventory;	// 最小库存
	private OrganizationEntity vendor;			// 供应商
	private MaterialEntity material;			// 物料
	private Double stockQty;					// 库存量
	
	
	private Double eoq;							//订货批量
	private String unitName;					//单位
	private Double rop;							//再订货点
	private Double passageQty;					//在途数量
	private Double stockMaxQty;					// 最大库存
	private String purchasePeople;				//采购员
	private Timestamp purAdvanceTime;			//采购提前期
	
	private String batchNum;					//批次号
	private String referTo;						//参考
	private Integer stockStatus;				//状态(区分不同的库存:0表示同步接口库存,1表示VMI库存)
	
	private Double repleQty;					//建议补充数量
	
	//非持久化对象
	private long vendorId;
	private String vendorCode;
	private String vendorName;
	private long materialId;
	private String materialCode;
	private String materialName;
	private Double stockMinQty; 
	
	
	
	

	@ManyToOne
	@JoinColumn(name="store_id")
	public StoreHouseEntity getStore() {
		return store;
	}

	public void setStore(StoreHouseEntity store) {
		this.store = store;
	}

	@ManyToOne
	@JoinColumn(name="min_inventory_id")
	public MinInventoryEntity getMinInventory() {
		return minInventory;
	}

	public void setMinInventory(MinInventoryEntity minInventory) {
		this.minInventory = minInventory;
	}

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

	@Column(name="stock_qty")
	public Double getStockQty() {
		return stockQty;
	}

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	
	@Column(name="eoq")
	public Double getEoq() {
		return eoq;
	}

	public void setEoq(Double eoq) {
		this.eoq = eoq;
	}

	@Column(name="unit_name")
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name="rop")
	public Double getRop() {
		return rop;
	}

	public void setRop(Double rop) {
		this.rop = rop;
	}

	@Column(name="passage_qty")
	public Double getPassageQty() {
		return passageQty;
	}

	public void setPassageQty(Double passageQty) {
		this.passageQty = passageQty;
	}

	
	@Column(name="stock_max_qty")
	public Double getStockMaxQty() {
		return stockMaxQty;
	}

	public void setStockMaxQty(Double stockMaxQty) {
		this.stockMaxQty = stockMaxQty;
	}

	@Column(name="purchase_people")
	public String getPurchasePeople() {
		return purchasePeople;
	}

	public void setPurchasePeople(String purchasePeople) {
		this.purchasePeople = purchasePeople;
	}
	
	
	@Column(name="pur_advance_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getPurAdvanceTime() {
		return purAdvanceTime;
	}

	public void setPurAdvanceTime(Timestamp purAdvanceTime) {
		this.purAdvanceTime = purAdvanceTime;
	}
	
	@Column(name="batch_num")
	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	@Column(name="refer_to")
	public String getReferTo() {
		return referTo;
	}

	public void setReferTo(String referTo) {
		this.referTo = referTo;
	}
	
	@Column(name="stock_status")
	public Integer getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(Integer stockStatus) {
		this.stockStatus = stockStatus;
	}
	
	@Column(name="reple_qty")
	public Double getRepleQty() {
		return repleQty;
	}

	public void setRepleQty(Double repleQty) {
		this.repleQty = repleQty;
	}

	@Transient
	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	@Transient
	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	@Transient
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@Transient
	public long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
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

	@Transient
	public Double getStockMinQty() {
		return stockMinQty;
	}

	public void setStockMinQty(Double stockMinQty) {
		this.stockMinQty = stockMinQty;
	}

}
