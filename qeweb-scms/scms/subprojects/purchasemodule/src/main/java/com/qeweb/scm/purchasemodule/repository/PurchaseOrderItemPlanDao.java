package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;


public interface PurchaseOrderItemPlanDao extends BaseRepository<PurchaseOrderItemPlanEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderItemPlanEntity>{

	@Override
	public List<PurchaseOrderItemPlanEntity> findAll();
	
	public List<PurchaseOrderItemPlanEntity> findByIdIn(List<Long> ids);
	
	/**
	 * 获取已发布未确认的计划记录
	 * @param itemIds
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id in (?1) and a.confirmStatus = 0 and a.publishStatus=1")
	public List<PurchaseOrderItemPlanEntity> findPlanByItemIds(List<Long> itemIds);
	
	@Override
	public Page<PurchaseOrderItemPlanEntity> findAll(Pageable page);

	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id = ?1")
	public PurchaseOrderItemPlanEntity findPurchaseOrderItemPlanEntityByItem(long itemId);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id = ?1")
	public List<PurchaseOrderItemPlanEntity> findItemPlanEntitysByItem(long itemId);
	
	public List<PurchaseOrderItemPlanEntity> findPurchaseOrderItemPlanEntityByOrderItem(PurchaseOrderItemEntity item);

	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.itemNo = ?1 and a.material.id = ?2")
	public PurchaseOrderItemPlanEntity findPurchaseOrderItemPlanEntityByItemNoAndMaterialId(int itemNo, long materialId);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.order.id=?1")
	public List<PurchaseOrderItemPlanEntity> findByOrderId(Long orderId);

	@Query("from PurchaseOrderItemPlanEntity a where a.order.orderCode=?1")
	public List<PurchaseOrderItemPlanEntity> findByOrderCode(String orderCode);
	
	//add by zhangjiejun 2015.10.13 start
	/**
	 * 通过item的id和Col5来查询供货plan集合
	 * @param 	orderItem	item对象
	 * @param 	col5		供货计划类型(0为普通供货。非0为负数收货端的补货，放的是负数收货的id)
	 * @return	供货plan集合
	 */
	public List<PurchaseOrderItemPlanEntity> findPurchaseOrderItemPlanEntityByOrderItemAndCol5(PurchaseOrderItemEntity orderItem, String col5);
	//add by zhangjiejun 2015.10.13 end
	
	@Query("from PurchaseOrderItemPlanEntity a where a.order.vendor.id=?1 and a.confirmStatus=?2 and a.deliveryStatus=?3")
	public List<PurchaseOrderItemPlanEntity> findByVendorIdAndConfirmAndDelivery(Long orderId,Integer confirmStatus,Integer deliveryStatus);
	
	/**
	 * 获取未发货国内供货计划
	 * @return
	 */
//	@Query("from PurchaseOrderItemPlanEntity where orderItem.publishStatus=1 and orderItem.orderStatus=1 and order.orderType=1 and confirmStatus = 1 and deliveryStatus=0")
	@Query("from PurchaseOrderItemPlanEntity where orderItem.orderStatus=1 and orderItem.publishStatus=1 and confirmStatus = 1  and order.orderType<>2  and deliveryStatus=0 and orderItem.closeStatus=0")
	public List<PurchaseOrderItemPlanEntity> findUndeliveryPlan();
	
	@Query("from PurchaseOrderItemPlanEntity where orderItem.publishStatus=1 and confirmStatus = 0")
	public List<PurchaseOrderItemPlanEntity> findUnConfirmPlan();
	
	/**
	 * 获取订单明细的id
	 * @return
	 */
	@Query(value="select distinct oi.id from qeweb_purchase_order_item oi left join qeweb_purchase_order o  on o.id=oi.order_id and o.order_type=?1",nativeQuery=true)
	public List<?> getDistinctItemIdA(Integer type);
	
	@Query(value="select distinct oi.id from qeweb_purchase_order_item oi left join qeweb_purchase_order o  on o.id=oi.order_id and o.order_type=?1 and o.vendor_id=?2",nativeQuery=true)
	public List<?> getDistinctItemIdB(Integer type,Long orgId);
}
