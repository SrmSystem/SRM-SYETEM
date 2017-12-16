package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
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

	@Query("from DeliveryItemEntity a where a.delivery= ?1 and a.abolished = ?2 order by a.request.rq desc")
	public List<DeliveryItemEntity> findByDeliveryAndAbolished(DeliveryEntity delivery,Integer abolished);
	
	@Query("from DeliveryItemEntity a where a.delivery.id = ?1 and a.abolished = ?2 and a.dn  is not null")
	public List<DeliveryItemEntity> findByDeliveryAndAbolishedAndIsNotNullDn(Long deliveryId,Integer abolished);
	public DeliveryItemEntity findDeliveryItemEntityByOrderItem(PurchaseOrderItemEntity item); 
	
	public List<DeliveryItemEntity> findDeliveryItemEntitysByOrderItemPlan(PurchaseOrderItemPlanEntity itemPlan);

	@Query(value="select t.request_item_plan_id from qeweb_delivery_item t where t.id = ?1",nativeQuery=true)
	public Long getRequestItemPlanmById(long id);

	@Query("from DeliveryItemEntity a where a.orderItem.id = ?1 and a.delivery.abolished = ?2 and a.delivery.status = ?3 and a.orderItemPlan.shipType=1")
	public List<DeliveryItemEntity> findByOrderItemIdAndDeliveryAbolishedAndDeliveryStatus(long orderItemId,
			Integer abolished, Integer status); 
	
	public List<DeliveryItemEntity> findByorderItemIdInAndAbolished(List<Long> ids , Integer abolished);
	
	
	/*@Query("from DeliveryItemEntity a where a.material.id = ?1 and  a.createTime between ?2 and ?3")
	public List<DeliveryItemEntity> findByMaterialId(Long materialId,Timestamp start,Timestamp end);
	*/
	@Query("from DeliveryItemEntity a where a.orderItemPlan.id = ?1  and a.abolished = 0   order by createTime")
	public List<DeliveryItemEntity> findDeliveryItemEntitysByOrderItemPlanId(Long planId);
	
	/**
	 * 根据dn查发货明细
	 * @param dn
	 * @return
	 */
	@Query("from DeliveryItemEntity a where a.dn = ?1 and a.abolished=0 and a.delivery.shipType=?2")
	public DeliveryItemEntity findDeliveryItemByDnAndShipType(String dn,int shipType);
	
	@Query("from DeliveryItemEntity a where a.dn = ?1 and a.abolished=0 and a.delivery.shipType=-1")
	public List<DeliveryItemEntity> findReplDeliveryItemByDn(String dn);
	
	@Query("from DeliveryItemEntity a where a.dn = ?1 and a.delivery.deliveryCode=?2 and a.abolished=0 and a.delivery.shipType=?3")
	public DeliveryItemEntity findDeliveryItemByDnAndDeliveryCodeAndShipType(String dn,String deliveryCode,int shipType);

	public DeliveryItemEntity findById(Long itemId);
	
	
/*	//根据要货计划查询已审核发货单
	@Query("from DeliveryItemEntity a where a.request.id = ?1 and  a.delivery.auditStatus =1   and  a.abolished=0  ")
	public List<DeliveryItemEntity> findDeliveryItemByGoodsRequest(Long goodsId);
	*/
	
	/*//根据要货计划查询不等于已审核发货单
	@Query("from DeliveryItemEntity a where a.request.id = ?1 and  a.delivery.auditStatus !=1   and  a.abolished=0  ")
	public List<DeliveryItemEntity> findDeliveryItemByGoodsRequestNotAudit(Long goodsId);*/
	
	//根据要货计划查询发货单
	@Query("from DeliveryItemEntity a where a.request.id = ?1 and  a.abolished=0 and a.delivery.shipType=1")
	public List<DeliveryItemEntity> findDeliveryItemEntitysBygoosdId(Long planId);
	
	//订单明细：ASN发货数量=根据订单明细查所有ASN发货数量[正常发货单]
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.abolished=0 and a.delivery.shipType=1")
	public Double findDlvQtyByItemId(long orderItemId);
	
	//订单明细：ASN已发货数量=根据订单明细查所有ASN发货数量
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.abolished=0 and a.delivery.deliveryStatus=1 and  a.delivery.shipType=1")
	public Double findHasDlvQtyByItemId(long orderItemId);
	
	//订单明细：ASN已发货数量=根据订单明细查所有ASN发货数量
    @Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.abolished=0 and  a.delivery.abolished=0 and a.delivery.deliveryStatus=1 and  a.receiveStatus=0")
	public Double findOnWayQtyByItemId(long orderItemId);
	
	//供货关系：发货数量=根据供货关系和发货状态查发货数量(已发或者未发)
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.id=?1 and a.delivery.deliveryStatus=?2 and a.abolished=0")
	public Double findDlvQtyByPlanId(long orderItemPlanId,Integer deliveryStatus);
	
	//供货关系：在途数量=已发货未收货
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.id=?1 and a.abolished=0 and  a.delivery.abolished=0 and a.delivery.deliveryStatus=1 and  a.receiveStatus=0")
	public Double findOnWayQtyByPlanId(long orderItemPlanId);
	
	//供货关系：在途数量=已发货未收货
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.id in(?1) and a.delivery.deliveryStatus=1 and a.receiveStatus=0 and a.abolished=0")
	public Double findOnWayQtyByPlanIds(List<Long> orderItemPlanIds);
	
	//订单明细：已发货数量=根据订单明细查所有已发货数量
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.delivery.deliveryStatus=1 and a.abolished=0 and a.delivery.shipType=1")
	public Double findHasDlvQtyByOrderItemId(long orderItemId);
		
	//订单明细:在途数量=根据订单明细查所有已发货数量未收
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.delivery.deliveryStatus=1 and a.receiveStatus=0 and a.abolished=0 and a.delivery.shipType=1")
	public Double findOnWayQtyByOrderItemId(long orderItemId);
		
	//订单明细：根据订单明细查未发货的发货明细，按照审核状态升序
	@Query("select a from DeliveryItemEntity a where a.orderItem.id=?1 and a.delivery.deliveryStatus=0 and a.abolished=0 and a.delivery.shipType=1 order by a.delivery.auditStatus asc,a.request.rq desc")
	public List<DeliveryItemEntity> findNoDlvItemByOrderItemIdOrderByAuditStatusAndRq(long orderItemId);
	
	/////////补货开始////////////////////////////
	//收货明细：ASN发货数量=根据收货明细查所有ASN发货数量[补货发货单]
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.recItemId=?1 and a.abolished=0 and a.delivery.shipType=-1")
	public Double findDlvQtyByRecItemId(long recItemId);
	
	//收货明细：ASN已发货数量=根据收货明细查所有ASN发货数量
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.recItemId=?1 and a.abolished=0 and a.delivery.deliveryStatus=1 and  a.delivery.shipType=-1")
	public Double findHasDlvQtyByRecItemId(long recItemId);
	
	
	//收货明细:在途数量=根据订单明细查所有已发货数量未收
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.recItemId=?1 and a.delivery.deliveryStatus=1 and a.receiveStatus=0 and a.abolished=0 and a.delivery.shipType=-1")
	public Double findOnWayQtyByRecItemId(long recItemId);
	
	
	//供货计划：根据供货计划查未发货的发货明细，按照审核状态升序
	@Query("select a from DeliveryItemEntity a where a.orderItemPlan.id=?1 and a.receiveStatus=0 and a.abolished=0 and a.delivery.shipType=-1 order by a.delivery.deliveryStatus asc,a.delivery.auditStatus asc,a.request.rq desc")
	public List<DeliveryItemEntity> findNoRecDlvItemByPlanIdOrderByAuditStatusAndRq(long planId);
	
	////////补货结束/////////////////////////////
	
	////////【普通+补货】开始/////////////////////////
	//供货计划：根据供货计划ID查所有的普通补货发货数量[普通+补货]
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.id in(?1) and a.delivery.deliveryStatus=1  and a.abolished=0")
	public Double findHasDlvQtyByPlanIds(List<Long> orderItemPlans);
	
	//订单明细：已发货数量=根据订单明细查所有已发货数量[普通+补货]
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.delivery.deliveryStatus=1 and a.abolished=0")
	public Double findAllHasDlvQtyByOrderItemId(long orderItemId);
	
	//供货计划：已收货数量
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItemPlan.id=?1 and a.delivery.deliveryStatus=1 and a.abolished=0 and a.receiveStatus=1")
	public Double findHasDlvQtyRecByPlanId(long planId);
	
	//订单明细：已收货数量
	@Query("select sum(a.deliveryQty) from DeliveryItemEntity a where a.orderItem.id=?1 and a.delivery.deliveryStatus=1 and a.abolished=0 and a.receiveStatus=1 and a.delivery.shipType=-1")
	public Double findHasDlvQtyRecByItemId(long planId);
	////////【普通+补货】结束///////////////////////
}
