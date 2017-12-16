package com.qeweb.scm.epmodule.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.EPBaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * 询价物料实体类
 * @author ronnie
 *
 */
@Entity
@Table(name = "QEWEB_EP_MATERIAL")
public class EpMaterialEntity extends EPBaseEntity {

	// 无料号手工录入时使用
	private EpPriceEntity price; // 询价单主表
	private EpModuleEntity module; // 报价模板
	private Long categoryId; // 品类ID
	private String categoryCode; // 品类编码
	private String categoryName; // 品类名称
	private Long materialId; // 物料id
	private MaterialEntity material;
	
	private String materialCode; // 物料编码
	private String materialName; // 物料名称
	private String materialSpec; // 物料规格
	private String materialUnit; // 物料单位

	private String warranty;	  //保修期
	private Double freight;		//运输费
	private String expectedBrand; // 期望品牌
	private String manufacturer; // 生产厂家
	private String materialComposition; // 材质构成
	private Double planPurchaseQty; // 预计采购数量
	private Timestamp arrivalTime; // 期望到达时间
	private String remarks; // 备注

	private Double lowestCoopPrice; // 采购商比价页面展示历史合作最低价
	private Double currentLowestQuotePrice; // 采购商比价页面展示当前最低报价
	private Double currentHightestQuotePrice; // 采购商比价页面展示当前最高报价
	
	private String compareMaterialCode;		//类比零部件编码
	private Double compareMaterialPrice;	//类比零部件现价
	private String compareVendorName;		//类比零部件供应商
	private String productStatusDiffer;		//产品状态差异说明

	@ManyToOne
	@JoinColumn(name="ENQUIRE_PRICE_ID")
	public EpPriceEntity getPrice() {
		return price;
	}

	public void setPrice(EpPriceEntity price) {
		this.price = price;
	}

	@ManyToOne
	@JoinColumn(name="MODULE_ID")
	public EpModuleEntity getModule() {
		return module;
	}

	public void setModule(EpModuleEntity module) {
		this.module = module;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	
	

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialSpec() {
		return materialSpec;
	}

	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}

	public String getMaterialUnit() {
		return materialUnit;
	}

	public void setMaterialUnit(String materialUnit) {
		this.materialUnit = materialUnit;
	}
	
	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getExpectedBrand() {
		return expectedBrand;
	}

	public void setExpectedBrand(String expectedBrand) {
		this.expectedBrand = expectedBrand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getMaterialComposition() {
		return materialComposition;
	}

	public void setMaterialComposition(String materialComposition) {
		this.materialComposition = materialComposition;
	}

	public Double getPlanPurchaseQty() {
		return planPurchaseQty;
	}

	public void setPlanPurchaseQty(Double planPurchaseQty) {
		this.planPurchaseQty = planPurchaseQty;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Timestamp getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Timestamp arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getLowestCoopPrice() {
		return lowestCoopPrice;
	}

	public void setLowestCoopPrice(Double lowestCoopPrice) {
		this.lowestCoopPrice = lowestCoopPrice;
	}

	public Double getCurrentLowestQuotePrice() {
		return currentLowestQuotePrice;
	}

	public void setCurrentLowestQuotePrice(Double currentLowestQuotePrice) {
		this.currentLowestQuotePrice = currentLowestQuotePrice;
	}

	public Double getCurrentHightestQuotePrice() {
		return currentHightestQuotePrice;
	}

	public void setCurrentHightestQuotePrice(Double currentHightestQuotePrice) {
		this.currentHightestQuotePrice = currentHightestQuotePrice;
	}

	public String getCompareMaterialCode() {
		return compareMaterialCode;
	}

	public void setCompareMaterialCode(String compareMaterialCode) {
		this.compareMaterialCode = compareMaterialCode;
	}

	public Double getCompareMaterialPrice() {
		return compareMaterialPrice;
	}

	public void setCompareMaterialPrice(Double compareMaterialPrice) {
		this.compareMaterialPrice = compareMaterialPrice;
	}

	public String getCompareVendorName() {
		return compareVendorName;
	}

	public void setCompareVendorName(String compareVendorName) {
		this.compareVendorName = compareVendorName;
	}

	public String getProductStatusDiffer() {
		return productStatusDiffer;
	}

	public void setProductStatusDiffer(String productStatusDiffer) {
		this.productStatusDiffer = productStatusDiffer;
	}
}
