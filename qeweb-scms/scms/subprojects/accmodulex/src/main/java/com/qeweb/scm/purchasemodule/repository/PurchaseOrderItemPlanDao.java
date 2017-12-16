package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
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
	
	public PurchaseOrderItemPlanEntity findById(Long id);
	
	/**
	 * 获取已发布未确认的计划记录
	 * @param itemIds
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id in (?1) and a.confirmStatus = 0 and a.publishStatus=1 and a.shipType=?2")
	public List<PurchaseOrderItemPlanEntity> findPlanByItemIds(List<Long> itemIds,int shipType);
	
	@Override
	public Page<PurchaseOrderItemPlanEntity> findAll(Pageable page);

	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id = ?1 and a.itemNo= ?2 and a.material.code =?3 and a.requestTime =?4 and a.abolished=0 and a.shipType=1")
	public PurchaseOrderItemPlanEntity findPurchaseOrderItemPlanEntityByItem(long itemId,Integer itemNo,String materialCode,Timestamp requestTime);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id = ?1  and a.abolished=0 and a.shipType=1")
	public List<PurchaseOrderItemPlanEntity> findItemPlanEntitysByItem(long itemId);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem = ?1  and a.abolished=0 and a.shipType=1")
	public List<PurchaseOrderItemPlanEntity> findPurchaseOrderItemPlanEntityByOrderItem(PurchaseOrderItemEntity item);

	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.itemNo = ?1 and a.material.id = ?2  and a.shipType=1")
	public PurchaseOrderItemPlanEntity findPurchaseOrderItemPlanEntityByItemNoAndMaterialId(int itemNo, long materialId);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.order.id=?1  and a.shipType=1 and a.abolished=0")
	public List<PurchaseOrderItemPlanEntity> findByOrderId(Long orderId);

	@Query("from PurchaseOrderItemPlanEntity a where a.order.orderCode=?1  and a.shipType=1 and a.abolished=0")
	public List<PurchaseOrderItemPlanEntity> findByOrderCode(String orderCode);
	
	
	/**
	 * 通过要货计划的id获取供货计划
	 * 
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and abolished = 0 and shipType=1")
	public List<PurchaseOrderItemPlanEntity> getPoItemplanListBygoodsId(Long goodsId);
	
	
	/**
	 * 通过要货计划的id获取未发布供货计划
	 * 
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and abolished = 0 and  publishStatus = 0  and shipType=1")
	public List<PurchaseOrderItemPlanEntity> getUnPublishPoItemplanListBygoodsId(Long goodsId);
	
	
	/**
	 * 获取已发布未确认的计划记录(通过要货计划id)
	 * @param itemIds
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and  publishStatus = 1  and   confirmStatus = 0 and  abolished = 0  and shipType=1")
	public List<PurchaseOrderItemPlanEntity> getNotConfirmPoItemplanListBygoodsId(Long goodsId);
	
	/**
	 * 通过要货计划的id获取确认供货计划
	 * 
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and abolished = 0 and publishStatus = 1  and confirmStatus = 1  and shipType=1")
	public List<PurchaseOrderItemPlanEntity> getConfirmPoItemplanListBygoodsId(Long goodsId);
	
	
	
	
	@Query("from PurchaseOrderItemPlanEntity a where a.orderItem.id = ?1  and a.abolished=0 and  a.shipType=1 order by a.publishStatus asc,a.confirmStatus asc, a.purchaseGoodsRequest.rq desc")
	public List<PurchaseOrderItemPlanEntity> findItemPlanEntitysByItemOrderByRequest(long itemId);
	
	//通过订单行的id获取在圖數量
	@Query("select sum(pp.onwayQty) from PurchaseOrderItemPlanEntity pp where pp.orderItem.id = ?1  and pp.abolished=0 and pp.shipType=1")
	public Double getOrderItemOnwayQty(Long orderItemId);
	
	//通过订单行的id获取未發數量
	@Query("select sum(pp.undeliveryQty) from PurchaseOrderItemPlanEntity pp where pp.orderItem.id = ?1  and pp.abolished=0 and pp.shipType=1")
	public Double getOrderItemUndeliveryQty(Long orderItemId);
	
	//通过订单行的id获取订单数量
	@Query("select sum(pp.orderQty) from PurchaseOrderItemPlanEntity pp where pp.orderItem.id = ?1 and pp.abolished=0 and pp.shipType=1")
	public Double getOrderItemOrderQty(Long orderItemId);
	
	/**
	 * 获取不等于未确认的计划记录（包括驳回和拒绝驳回）(通过要货计划id)
	 * modify by chao.gu 增加已发布的
	 * @param itemIds
	 * @return
	 */
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and confirmStatus != 1 and  abolished = 0 and shipType=1 and publishStatus=1")
	public List<PurchaseOrderItemPlanEntity> getNotInConfirmPoItemplanListBygoodsId(Long goodsId);
	
	
	/**
	 * 根据id集合查供货计划并按要求到货时间升序
	 */
	@Query("from PurchaseOrderItemPlanEntity a where a.id in(?1) order by a.requestTime asc")
	public List<PurchaseOrderItemPlanEntity> findByIdInOrderByReqTime(List<Long> ids);
	
	//////////以下为补货//////////////////////////
	
	/**
	 *根据收货明细id查补货的供货关系
	 */
	@Query("from PurchaseOrderItemPlanEntity where recItemId= ?1 and  abolished = 0 and shipType=-1")
	public List<PurchaseOrderItemPlanEntity>  findByReceiveItemId(Long recItemId);
	
	//通过收货明细的id获取订单数量
	@Query("select sum(pp.orderQty) from PurchaseOrderItemPlanEntity pp where pp.recItemId = ?1 and pp.abolished=0 and pp.shipType=-1")
	public Double getSumPlanOrderQtyByReceiveItemId(Long receiveItemId);
	
	@Query("from PurchaseOrderItemPlanEntity a where a.recItemId = ?1  and a.abolished=0  and a.shipType=-1 order by a.publishStatus asc,a.confirmStatus asc, a.purchaseGoodsRequest.rq desc")
	public List<PurchaseOrderItemPlanEntity> findItemPlanEntitysByRecItemOrderByRequest(long recItemId);
	
	//通过普通供货计划id查所有的补货的供货计划id
	@Query("select pp.id from PurchaseOrderItemPlanEntity pp where pp.sourceOrderItemPlanId = ?1 and pp.abolished=0 and pp.shipType=-1")
	public List<Long> findReplPlanIdsBySourcePlanId(Long sourcePlanId);
	
	/////////补货结束////////////////////////////////
	
	
	////////【普通+补货】开始/////////////////////////
	//通过id获取订单数量【普通+补货】
	@Query("select sum(pp.orderQty) from PurchaseOrderItemPlanEntity pp where pp.id in(?1) and pp.abolished=0")
	public Double findOrderQtyByIds(List<Long> ids);
	
	//通过id获取已发货数量【普通+补货】
	@Query("select sum(pp.deliveryQty) from PurchaseOrderItemPlanEntity pp where pp.id in(?1) and pp.abolished=0")
	public Double findDeliveryQtyByIds(List<Long> ids);
	
	//通过id获取已创建未发货数量【普通+补货】
	@Query("select sum(pp.toDeliveryQty) from PurchaseOrderItemPlanEntity pp where pp.id in(?1) and pp.abolished=0")
	public Double findToDeliveryQtyByIds(List<Long> ids);
	
	//通过订单明细差所有已发数量【普通+补货】
	@Query("select sum(pp.deliveryQty) from PurchaseOrderItemPlanEntity pp where  pp.orderItem.id = ?1 and pp.abolished=0")
	public Double findDeliveryQtyByOrderItemId(Long id);
	
	//通过订单明细差所有已创建未发数量【普通+补货】
	@Query("select sum(pp.toDeliveryQty) from PurchaseOrderItemPlanEntity pp where  pp.orderItem.id = ?1 and pp.abolished=0")
	public Double findToDeliveryQtyByOrderItemId(Long id);
	////////【普通+补货】结束/////////////////////////
	
	@Query("from PurchaseOrderItemPlanEntity where goodsRequestId= ?1 and confirmStatus = 0 and  abolished = 0 and shipType=?2")
	public List<PurchaseOrderItemPlanEntity> getUnConfirmPoItemplanListBygoodsId(Long goodsId, int type );
}
