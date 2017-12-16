package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorBUEntity;


public interface BuyerMaterialRelDao extends BaseRepository<BuyerMaterialRelEntity, Serializable>,JpaSpecificationExecutor<BuyerMaterialRelEntity>{

	List<BuyerMaterialRelEntity> findByBuyerId(long buyerId);
	
	BuyerMaterialRelEntity findByBuyerIdAndMaterialId(long buyerId,long materialId);
	
	List<BuyerMaterialRelEntity> findByBuyerIdIn(List<Long> buyerIds);

}
