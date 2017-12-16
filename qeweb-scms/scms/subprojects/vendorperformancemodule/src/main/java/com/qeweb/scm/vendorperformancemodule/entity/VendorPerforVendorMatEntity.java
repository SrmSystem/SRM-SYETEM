package com.qeweb.scm.vendorperformancemodule.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 供应商物料管理
 * @author sxl
 *
 */
@Entity
@Table(name="qeweb_assess_vendor_mat")
public class VendorPerforVendorMatEntity  extends BaseEntity{
	
	private Long reviewsId;	
	private Long materialtypeId;	
	private VendorPerforMaterialTypeEntity mt;
	private VendorPerforReviewsEntity re;
	
	public Long getReviewsId() {
		return reviewsId;
	}
	public void setReviewsId(Long reviewsId) {
		this.reviewsId = reviewsId;
	}
	public Long getMaterialtypeId() {
		return materialtypeId;
	}
	public void setMaterialtypeId(Long materialtypeId) {
		this.materialtypeId = materialtypeId;
	}
	@ManyToOne
	@JoinColumn(name="materialtypeId",insertable=false,updatable=false)
	public VendorPerforMaterialTypeEntity getMt() {
		return mt;
	}
	public void setMt(VendorPerforMaterialTypeEntity mt) {
		this.mt = mt;
	}
	@ManyToOne
	@JoinColumn(name="reviewsId",insertable=false,updatable=false)
	public VendorPerforReviewsEntity getRe() {
		return re;
	}
	public void setRe(VendorPerforReviewsEntity re) {
		this.re = re;
	}
}
