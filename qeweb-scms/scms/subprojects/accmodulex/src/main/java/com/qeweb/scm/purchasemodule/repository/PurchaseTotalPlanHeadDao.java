package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanHeadEntity;



public interface PurchaseTotalPlanHeadDao extends BaseRepository<PurchaseTotalPlanHeadEntity, Serializable>,JpaSpecificationExecutor<PurchaseTotalPlanHeadEntity>{

	@Override
	public List<PurchaseTotalPlanHeadEntity> findAll();
	
	@Query("from PurchaseTotalPlanHeadEntity a where a.planItem.id = ?1 and a.isNew = 1 and a.abolished=0")
	public List<PurchaseTotalPlanHeadEntity> findNewPurchasePlanHeadByplanItemId(Long planItemId);
	
	@Modifying  
	@Query("update PurchaseTotalPlanHeadEntity a set a.isNew = 0  where a.planItem.id = ?1")
	public void invalidPurchaseTotalPlanHead(Long planItemId);
	
	@Query("from PurchaseTotalPlanHeadEntity a where a.planItem.id = ?1  and a.abolished=0")
	public List<PurchaseTotalPlanHeadEntity> findPurchasePlanHeadByplanItemId(Long planItemId);
	
	
/*	@Query(value="select a.* from QEWEB_PURCHASE_TOTAL_PLAN_ITEM b "
			+ "LEFT JOIN QEWEB_PURCHASE_TOTAL_PLAN_HEAD a on b.id = a.PLAN_ITEM_ID  "
			+ " LEFT JOIN qeweb_factory f ON  b.FACTORY_ID = f.id  "
			+ "LEFT JOIN qeweb_material m ON  b.MATERIAL_ID = m.id "
			+ "LEFT JOIN qeweb_user u ON  b.BUYER_ID = u.id  "
			+ " where  f.CODE = ?1 AND m.CODE = ?2 AND u.LOGIN_NAME = ?3  AND b.ABOLISHED = 0 AND b.IS_NEW =1",nativeQuery=true)*/
	
	@Query("from PurchaseTotalPlanHeadEntity a where  a.planItem.factory.code = ?1 and a.planItem.material.code= ?2 and a.planItem.purchasingGroup.code= ?3   and  a.planItem.plan.month = ?4   and a.planItem.abolished=0 and a.planItem.isNew = 1")
	public List<PurchaseTotalPlanHeadEntity> findPurchaseTotalPlanItemEntityByFactoryAndMaterialAndGroup(String factoryCode,String materialCode,String groupCode,String month);
	
}
