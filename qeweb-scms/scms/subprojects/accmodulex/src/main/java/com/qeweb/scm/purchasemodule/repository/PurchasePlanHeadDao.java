package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanHeadEntity;


public interface PurchasePlanHeadDao extends BaseRepository<PurchasePlanHeadEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanHeadEntity>{

	@Override
	public List<PurchasePlanHeadEntity> findAll();
	
	@Query("from PurchasePlanHeadEntity a where a.planItem.id = ?1 and a.isNew = 1 and a.abolished=0")
	public List<PurchasePlanHeadEntity> findNewPurchasePlanHeadByplanItemId(Long planItemId);
	
	@Modifying 
	@Query("update PurchasePlanHeadEntity a set a.isNew = 0  where a.planItem.id = ?1")
	public void invalidPurchasePlanHead(Long planItemId);
	
	@Query("from PurchasePlanHeadEntity a where a.planItem.id = ?1 and a.abolished=0")
	public List<PurchasePlanHeadEntity> findNewPurchasePlanHeadAllByplanItemId(Long planItemId);
	
}
