package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.DeliveryEntity;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;


public interface DeliveryItemDao extends BaseRepository<DeliveryItemEntity, Serializable>,JpaSpecificationExecutor<DeliveryItemEntity>{

	@Override
	public List<DeliveryItemEntity> findAll();
	
	@Override
	public Page<DeliveryItemEntity> findAll(Pageable page);

	@Query("from DeliveryItemEntity a where a.delivery.deliveryCode = ?1 and a.material.code = ?2 and a.itemNo = ?3")
	public List<DeliveryItemEntity> findDeliveryItemEntityByReceiveInfo(String deliveryCode, String materialCode, Integer itemNo);

	public List<DeliveryItemEntity> findByDelivery(DeliveryEntity delivery);

	public List<DeliveryItemEntity> findByCol9IsNullOrCol9Not(String isSync); 

	//add by zhangjiejun 2015.09.25 start
	/**
	 * 通过itemPlan查询发货明细对象	
	 * @param 	itemPlan	计划对象
	 * @return	发货明细对象
	 */
	public DeliveryItemEntity findDeliveryItemEntityByOrderItemPlan(PurchaseOrderItemPlanEntity itemPlan); 
	//add by zhangjiejun 2015.09.25 end
	
	public DeliveryItemEntity findDeliveryItemEntityByOrderItem(PurchaseOrderItemEntity item); 
	
	public List<DeliveryItemEntity> findDeliveryItemEntitysByOrderItemPlan(PurchaseOrderItemPlanEntity itemPlan);

	@Query(value="select t.request_item_plan_id from qeweb_delivery_item t where t.id = ?1",nativeQuery=true)
	public Long getRequestItemPlanmById(long id);

	@Query("from DeliveryItemEntity a where a.orderItem.id = ?1 and a.delivery.abolished = ?2 and a.delivery.status = ?3")
	public List<DeliveryItemEntity> findByOrderItemIdAndDeliveryAbolishedAndDeliveryStatus(long orderItemId,
			Integer abolished, Integer status);

	@Query("from DeliveryItemEntity a where a.requestItemPlan.id = ?1 and a.delivery.abolished = ?2 and a.delivery.deliveryStatus = ?3")
	public List<DeliveryItemEntity> findByRequestItemPlanIdAndDeliveryAbolishedAndDeliveryStatus(
			long requestItemPlanId, int i, int j); 
}
