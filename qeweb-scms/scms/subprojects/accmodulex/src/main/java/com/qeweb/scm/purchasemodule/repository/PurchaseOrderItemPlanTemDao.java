package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanTemEntity;


public interface PurchaseOrderItemPlanTemDao extends BaseRepository<PurchaseOrderItemPlanTemEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderItemPlanTemEntity>{

	@Override
	public List<PurchaseOrderItemPlanTemEntity> findAll();
	
	@Modifying
	@Query(value="delete from PurchaseOrderItemPlanTemEntity where goodsRequestId = ?1 and userId = ?2 ")
	void deleteByGoodsIdAndUseId(Long goodsId,Long userId );
	
	
	//根据要货计划查询临时表的数据
	@Query("from PurchaseOrderItemPlanTemEntity a where a.purchaseGoodsRequest.id = ?1 and  a.userId = 1  and  a.abolished= 0")
	public List<PurchaseOrderItemPlanTemEntity> findByGoodsId(Long goodsId);
	
}
