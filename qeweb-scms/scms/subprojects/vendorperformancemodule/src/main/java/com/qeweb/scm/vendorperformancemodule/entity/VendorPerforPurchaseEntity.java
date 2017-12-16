package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 采购额分界线
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_purchase")
public class VendorPerforPurchaseEntity  extends BaseEntity{
	
	private Long materialtypeId;	//物料类别ID
	
	private String materialtypeName;	//物料类别ID
	
	private String materialtypeNamef;	//物料类别ID
	
	private Long cycleId;	//周期ID
	
	private Integer purchaseNumber;//数量
	
	public Long getMaterialtypeId() {
		return materialtypeId;
	}
	public void setMaterialtypeId(Long materialtypeId) {
		this.materialtypeId = materialtypeId;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public Integer getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(Integer purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public String getMaterialtypeName() {
		return materialtypeName;
	}
	public void setMaterialtypeName(String materialtypeName) {
		this.materialtypeName = materialtypeName;
	}
	public String getMaterialtypeNamef() {
		return materialtypeNamef;
	}
	public void setMaterialtypeNamef(String materialtypeNamef) {
		this.materialtypeNamef = materialtypeNamef;
	}
}
