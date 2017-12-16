package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityInfoEntity;


public interface PurchasePlanVendorCapacityInfoDao extends BaseRepository<PurchasePlanVendorCapacityInfoEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanVendorCapacityInfoEntity>{

	@Override
	public List<PurchasePlanVendorCapacityInfoEntity> findAll();
	
	@Query("from PurchasePlanVendorCapacityInfoEntity a where a.planItem.id = ?1 and a.abolished=0")
	public PurchasePlanVendorCapacityInfoEntity findByPlanItemId(Long planId);
	
}
