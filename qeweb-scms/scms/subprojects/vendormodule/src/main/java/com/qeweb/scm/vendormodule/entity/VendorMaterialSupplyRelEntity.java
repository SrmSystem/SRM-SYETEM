package com.qeweb.scm.vendormodule.entity;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;

/**
 * 供货关系Entity
 * 供应商供货物料详情
 * @author lw
 * 创建时间：2015年6月15日09:22:42
 * 最后更新时间：2015年6月15日09:22:48
 * 最后更新人：lw
 */
@Entity
@Table(name = "QEWEB_VENDOR_MAT_SUPPLY_REL")
public class VendorMaterialSupplyRelEntity extends BaseEntity{

	private Long orgId;
	private Long vendorId;
	private String vendorName;
	private Long materialId;
	private String materialName;

	private Long bussinessRangeId;
	private String bussinessRangeName;
	private Long bussinessId;
	private String bussinessName;
	private Long brandId;
	private String brandName;
	private Long productLineId;
	private String productLineName;
	private Long factoryId;
	private String factoryName;
	private Integer ismain;
	private Double supplyCoefficient;			//供货系数
	private Long materialRelId;
	
	private Timestamp effectiveTime;
	
	private VendorMaterialRelEntity vendorMaterialRelEntity;
	private MaterialEntity materialEntity;
	private VendorBaseInfoEntity vendorBaseInfoEntity;
	private BussinessRangeEntity productLine;
	private BussinessRangeEntity bussinessRange;
	private BussinessRangeEntity bussinessType;
	private BussinessRangeEntity bussinessBrand;
	private FactoryEntity factory;
	
	private MaterialTypeEntity  materialType1;
	private MaterialTypeEntity  materialType2;
	private MaterialTypeEntity  materialType3;
	private MaterialTypeEntity  materialType4;
	
	
	//冗余字段
	private Integer brandCount;
	private String allBrand;
	private Double totalSupplyCoefficient; //总供货系数
	/*****透明字段******/
	private String biaoganSupplyCoefficient;
	
	
	
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Long getBussinessRangeId() {
		return bussinessRangeId;
	}
	public void setBussinessRangeId(Long bussinessRangeId) {
		this.bussinessRangeId = bussinessRangeId;
	}
	public Long getBussinessId() {
		return bussinessId;
	}
	public void setBussinessId(Long bussinessId) {
		this.bussinessId = bussinessId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getProductLineId() {
		return productLineId;
	}
	public void setProductLineId(Long productLineId) {
		this.productLineId = productLineId;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public Integer getIsmain() {
		return ismain;
	}
	public void setIsmain(Integer ismain) {
		this.ismain = ismain;
	}
	
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getBussinessRangeName() {
		return bussinessRangeName;
	}
	public void setBussinessRangeName(String bussinessRangeName) {
		this.bussinessRangeName = bussinessRangeName;
	}
	public String getBussinessName() {
		return bussinessName;
	}
	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public Long getMaterialRelId() {
		return materialRelId;
	}
	public void setMaterialRelId(Long materialRelId) {
		this.materialRelId = materialRelId;
	}
	public void setSupplyCoefficient(Double supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}
	public Double getSupplyCoefficient() {
		return supplyCoefficient;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@ManyToOne
	@JoinColumn(name = "materialId",insertable = false,updatable = false)
	public MaterialEntity getMaterialEntity() {
		return materialEntity;
	}
	public void setMaterialEntity(MaterialEntity materialEntity) {
		this.materialEntity = materialEntity;
	}
	@ManyToOne
	@JoinColumn(name = "vendorId",insertable = false,updatable = false)
	public VendorBaseInfoEntity getVendorBaseInfoEntity() {
		return vendorBaseInfoEntity;
	}
	public void setVendorBaseInfoEntity(VendorBaseInfoEntity vendorBaseInfoEntity) {
		this.vendorBaseInfoEntity = vendorBaseInfoEntity;
	}
	@ManyToOne
	@JoinColumn(name = "materialRelId",insertable = false,updatable = false)
	public VendorMaterialRelEntity getVendorMaterialRelEntity() {
		return vendorMaterialRelEntity;
	}
	public void setVendorMaterialRelEntity(
			VendorMaterialRelEntity vendorMaterialRelEntity) {
		this.vendorMaterialRelEntity = vendorMaterialRelEntity;
	}
	@Transient
	public Integer getBrandCount() {
		return brandCount;
	}
	public void setBrandCount(Integer brandCount) {
		this.brandCount = brandCount;
	}
	@Transient
	public String getAllBrand() {
		return allBrand;
	}
	public void setAllBrand(String allBrand) {
		this.allBrand = allBrand;
	}
	@ManyToOne
	@JoinColumn(name = "productLineId",insertable = false,updatable = false)
	public BussinessRangeEntity getProductLine() {
		return productLine;
	}
	public void setProductLine(BussinessRangeEntity productLine) {
		this.productLine = productLine;
	}
	@ManyToOne
	@JoinColumn(name = "bussinessRangeId",insertable = false,updatable = false)
	public BussinessRangeEntity getBussinessRange() {
		return bussinessRange;
	}
	public void setBussinessRange(BussinessRangeEntity bussinessRange) {
		this.bussinessRange = bussinessRange;
	}
	@ManyToOne
	@JoinColumn(name = "bussinessId",insertable = false,updatable = false)
	public BussinessRangeEntity getBussinessType() {
		return bussinessType;
	}
	public void setBussinessType(BussinessRangeEntity bussinessType) {
		this.bussinessType = bussinessType;
	}
	@ManyToOne
	@JoinColumn(name = "brandId",insertable = false,updatable = false)
	public BussinessRangeEntity getBussinessBrand() {
		return bussinessBrand;
	}
	public void setBussinessBrand(BussinessRangeEntity bussinessBrand) {
		this.bussinessBrand = bussinessBrand;
	}
	@ManyToOne
	@JoinColumn(name = "factoryId",insertable = false,updatable = false)
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	
	@Transient
	public MaterialTypeEntity getMaterialType1() {
		return materialType1;
	}
	public void setMaterialType1(MaterialTypeEntity materialType1) {
		this.materialType1 = materialType1;
	}
	@Transient
	public MaterialTypeEntity getMaterialType2() {
		return materialType2;
	}
	public void setMaterialType2(MaterialTypeEntity materialType2) {
		this.materialType2 = materialType2;
	}
	@Transient
	public MaterialTypeEntity getMaterialType3() {
		return materialType3;
	}
	public void setMaterialType3(MaterialTypeEntity materialType3) {
		this.materialType3 = materialType3;
	}
	
	@Transient
	public MaterialTypeEntity getMaterialType4() {
		return materialType4;
	}
	
	public void setMaterialType4(MaterialTypeEntity materialType4) {
		this.materialType4 = materialType4;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(Timestamp effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	@Transient
	public Double getTotalSupplyCoefficient() {
		return totalSupplyCoefficient;
	}
	public void setTotalSupplyCoefficient(Double totalSupplyCoefficient) {
		this.totalSupplyCoefficient = totalSupplyCoefficient;
	}
	@Transient
	public String getBiaoganSupplyCoefficient() {
		return biaoganSupplyCoefficient;
	}
	public void setBiaoganSupplyCoefficient(String biaoganSupplyCoefficient) {
		this.biaoganSupplyCoefficient = biaoganSupplyCoefficient;
	}
}
