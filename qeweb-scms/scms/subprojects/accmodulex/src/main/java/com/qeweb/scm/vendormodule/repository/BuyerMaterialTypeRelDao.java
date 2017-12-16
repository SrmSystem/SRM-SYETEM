package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.BuyerMaterialTypeRelEntity;


public interface BuyerMaterialTypeRelDao extends BaseRepository<BuyerMaterialTypeRelEntity, Serializable>,JpaSpecificationExecutor<BuyerMaterialTypeRelEntity>{

	List<BuyerMaterialTypeRelEntity> findByBuyerId(long buyerId);
	
	BuyerMaterialTypeRelEntity findByBuyerIdAndMaterialTypeId(long buyerId,long materialTypeId);
	
	List<BuyerMaterialTypeRelEntity> findByBuyerIdIn(List<Long> buyerIds);
	
	List<BuyerMaterialTypeRelEntity> findByMaterialTypeId(long materialTypeId);

}
