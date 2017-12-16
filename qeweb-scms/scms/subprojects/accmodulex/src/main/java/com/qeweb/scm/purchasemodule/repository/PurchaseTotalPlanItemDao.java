package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanItemEntity;


public interface PurchaseTotalPlanItemDao extends BaseRepository<PurchaseTotalPlanItemEntity, Serializable>,JpaSpecificationExecutor<PurchaseTotalPlanItemEntity>{

	@Override
	public List<PurchaseTotalPlanItemEntity> findAll();
	
	
	@Query("select a from PurchaseTotalPlanItemEntity a where a.plan.id = ?1 and a.factory.code = ?2 and a.material.code= ?3 and a.purchasingGroup.code= ?4  and a.abolished=0 and a.isNew = 1")
	public PurchaseTotalPlanItemEntity findPurchaseTotalPlanItemEntityByMainId(long planId, String factoryCode,String materialCode,String groupCode);
	
	@Modifying 
	@Query("update PurchaseTotalPlanItemEntity set isNew = 0  where id = ?1")
	public void invalidPurchaseTotalPlanItem(Long planItemId);
	
	
	
	@Query("select a from PurchaseTotalPlanItemEntity a where a.plan.month = ?1  and a.factory.code = ?2 and a.material.code= ?3  and a.abolished=0 and a.isNew = 1")
	public PurchaseTotalPlanItemEntity findPurchaseTotalPlanItemEntityByMonthAndFaAndMaAnGr(String month, String factoryCode,String materialCode);
	
	
	
	public PurchaseTotalPlanItemEntity findById(Long id);
	
	

}
