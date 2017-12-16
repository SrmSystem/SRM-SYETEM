package com.qeweb.scm.vendorperformancemodule.vo;

import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforReviewsEntity;

public class BaseInfoAndPerforReviewsVo {

	private Long id;
	
	private VendorBaseInfoEntity baseInfoEntity;
	
	private VendorPerforReviewsEntity reviewsEntity;
	
	private Integer abolished;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VendorBaseInfoEntity getBaseInfoEntity() {
		return baseInfoEntity;
	}

	public void setBaseInfoEntity(VendorBaseInfoEntity baseInfoEntity) {
		this.baseInfoEntity = baseInfoEntity;
	}

	public VendorPerforReviewsEntity getReviewsEntity() {
		return reviewsEntity;
	}

	public void setReviewsEntity(VendorPerforReviewsEntity reviewsEntity) {
		this.reviewsEntity = reviewsEntity;
	}

	public Integer getAbolished() {
		return abolished;
	}

	public void setAbolished(Integer abolished) {
		this.abolished = abolished;
	}
}
