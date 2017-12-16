package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVenodrItemEntity;


public interface PurchasePlanVendorItemDao extends BaseRepository<PurchasePlanVenodrItemEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanVenodrItemEntity>{

	@Override
	public List<PurchasePlanVenodrItemEntity> findAll();
	
	public List<PurchasePlanVenodrItemEntity> findByPlanId(Long planId);
	
	@Override
	public Page<PurchasePlanVenodrItemEntity> findAll(Pageable page);

	@Query("select a from PurchasePlanVenodrItemEntity a where a.vendorPlan.id = ?1 and a.material.code = ?2 and itemNo = ?3 and a.planRecTime= ?4 and a.abolished=0")
	public PurchasePlanVenodrItemEntity findPurchasePlanVenodrItemEntityByVendorPlan(long vendorPlanId, String materialCode, Integer itemNo,Timestamp planRecTime);
}
