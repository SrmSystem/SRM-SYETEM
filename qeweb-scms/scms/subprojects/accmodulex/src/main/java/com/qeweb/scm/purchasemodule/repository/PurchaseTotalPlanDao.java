package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanEntity;


public interface PurchaseTotalPlanDao extends BaseRepository<PurchaseTotalPlanEntity, Serializable>,JpaSpecificationExecutor<PurchaseTotalPlanEntity>{

	@Override
	public List<PurchaseTotalPlanEntity> findAll();
	
	@Query("select a from PurchaseTotalPlanEntity a where a.month = ?1 and a.createUserId = ?2 and a.abolished=0") 
	public PurchaseTotalPlanEntity findPurchasePlanEntityByMonthAndCreateUserId(String month,Long id);
}
