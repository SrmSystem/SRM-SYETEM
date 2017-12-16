package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;


public interface PurchasePlanItemDao extends BaseRepository<PurchasePlanItemEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanItemEntity>{

	@Override
	public List<PurchasePlanItemEntity> findAll();
	
	@Override
	public Page<PurchasePlanItemEntity> findAll(Pageable page);

	@Query("select a from PurchasePlanItemEntity a where a.plan.id = ?1 and a.itemNo = ?2")
	public PurchasePlanItemEntity findPurchasePlanItemEntityByMainId(long planId, Integer itemNo);
	
	@Query(value="select sum(total_plan_qty) from qeweb_purchase_plan_item where material_id=?1",nativeQuery=true)
	public BigDecimal findPlanQtyByMaterial(Long materialId);
}
