package com.qeweb.scm.vendormodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;

/**
 * 物料关系Entity
 * 供应商物料关系
 * @author lw
 * 创建时间：2015年6月15日09:23:32
 * 最后更新时间：2015年6月15日09:23:36
 * 最后更新人：lw
 */
@Entity
@Table(name = "QEWEB_VENDOR_MATERIAL_REL")
public class VendorMaterialRelEntity extends BaseEntity{
    
	private Long orgId;
	private Long vendorId;
	private String vendorName;
	private Long materialId;
	private String materialName;
	private Integer status;
	private String dataFrom;
	private String carModel;//标杆车型

	
	//级联对象
	private VendorBaseInfoEntity vendor;
	private MaterialEntity material;
	//非持久化
	private Double supplyCoefficient;			//供货系数
	
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	
	
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

	@ManyToOne
	@JoinColumn(name = "vendorId", insertable = false, updatable = false)
	public VendorBaseInfoEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorBaseInfoEntity vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name = "materialId", insertable = false, updatable = false)
	public MaterialEntity getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Transient
	public Double getSupplyCoefficient() {
		return supplyCoefficient;
	}

	public void setSupplyCoefficient(Double supplyCoefficient) {
		this.supplyCoefficient = supplyCoefficient;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}

	public String getCol7() {
		return col7;
	}

	public void setCol7(String col7) {
		this.col7 = col7;
	}

	public String getCol8() {
		return col8;
	}

	public void setCol8(String col8) {
		this.col8 = col8;
	}
	
	

}
