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
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;


public interface ReceiveItemDao extends BaseRepository<ReceiveItemEntity, Serializable>,JpaSpecificationExecutor<ReceiveItemEntity>{

	@Override
	public List<ReceiveItemEntity> findAll();
	
	@Override
	public Page<ReceiveItemEntity> findAll(Pageable page);
	
	@Query("select sum(ri.receiveQty) from ReceiveItemEntity ri where ri.deliveryItem.id = ?1")
	public Double getDeliveryItemReceiveQty(Long deliveryItemId);
	
	@Query("from ReceiveItemEntity a where a.receive.receiveCode = ?1 and a.deliveryItem.id = ?2")
	public ReceiveItemEntity getReceiveItemEntityByCode(String receiveCode, long deliveryItmeId);
	
	//add by zhangjiejun 2015.09.18 end
//	/**
//	 * 根据voucher查询明细对象
//	 * @param 	voucher	队列号
//	 * @return	明细对象
//	 */
//	ReceiveItemEntity findByVoucher(int voucher);
	
	/**
	 * 根据voucher查询明细对象
	 * @param 	voucher	队列号
	 * @return	明细对象集合
	 */
	List<ReceiveItemEntity> findByVoucher(int voucher);
	//add by zhangjiejun 2015.09.18 end

	public List<ReceiveItemEntity> findByReceive(ReceiveEntity receiveEntity);
	
//	//add by zhangjiejun 2015.10.08 start
//	/**
//	 * 通过id数组集合，批量删除
//	 * @param 	ids	id数组集合
//	 */
//	public void deleteByIdIn(long[] ids);
//	//add by zhangjiejun 2015.10.08 end
	
	public List<ReceiveItemEntity> findByOrderItem(PurchaseOrderItemEntity orderItem);

	//add by zhangjiejun 2015.10.19 start
	/**
	 * 通过货运单号， 采购单号， 采购单行号查询集合（理论上只有一个）
	 * @param 	receiveCode	收货单号
	 * @param 	orderCode	订单号
	 * @param 	itemNo		订单明细行号
	 * @return	收货明细集合
	 */
	@Query("from ReceiveItemEntity a where a.receive.receiveCode = ?1 and a.orderItem.order.orderCode = ?2 and a.orderItem.itemNo = ?3")
	public List<ReceiveItemEntity> findByReceiveCodeAndOrderCodeAndItemNo(String receiveCode, String orderCode, int itemNo);
	//add by zhangjiejun 2015.10.19 end

	@Query(value="select a.delivery_item_id from qeweb_receive_item a where a.id = ?1",nativeQuery=true)
	public long getDeliveryItemIdByReceiveItemId(long id);

	public List<ReceiveItemEntity> findByAttr26(String voucher);

	//add by zhangjiejun 2016.01.26 start
	/**
	 * 国内，通过货运单号， 采购单号， 采购单行号， 供应商批次号查询集合（理论上只有一个）
	 * @param 	receiveCode	收货单号
	 * @param 	orderCode	订单号
	 * @param 	itemNo		订单明细行号
	 * @param 	attr5		供应商批次号
	 * @return	收货明细集合
	 */
	@Query("from ReceiveItemEntity a where a.receive.receiveCode = ?1 and a.orderItem.order.orderCode = ?2 and a.orderItem.itemNo = ?3 and a.attr5 = ?4")
	public List<ReceiveItemEntity> findByReceiveCodeAndOrderCodeAndItemNoAndAttr5(String receiveCode, String orderCode, int itemNo, String attr5);
	

	/**
	 * 国外，通过货运单号， 采购单号， 采购单行号， 产品批注号查询集合（理论上只有一个）
	 * @param 	receiveCode	收货单号
	 * @param 	orderCode	订单号
	 * @param 	itemNo		订单明细行号
	 * @param 	col8		产品批注号
	 * @return	收货明细集合
	 */
	@Query("from ReceiveItemEntity a where a.receive.receiveCode = ?1 and a.orderItem.order.orderCode = ?2 and a.orderItem.itemNo = ?3 and a.deliveryItem.col8 = ?4")
	public List<ReceiveItemEntity> findByReceiveCodeAndOrderCodeAndItemNoAndCol8(String receiveCode, String orderCode, int itemNo, String col8);
	//add by zhangjiejun 2016.01.26 end
	
	public ReceiveItemEntity findById(Long id);
	
	
	public List<ReceiveItemEntity> findByorderItemIdInAndAbolished(List<Long> ids , Integer abolished);
	
	@Query("from ReceiveItemEntity a where a.deliveryItem.id = ?1")
	public ReceiveItemEntity getReceiveItemByDlv(long dlvItemId);
	
	/**
	 * 获取供应商在一段时间内的未对账明细
	 * @author chao.gu
	 * 20170710
	 * @param begin
	 * @param end
	 * @param buyerId
	 * @param vendorId
	 * @return
	 */
	@Query("from ReceiveItemEntity a where a.receive.createTime >= ?1 and a.receive.createTime < ?2 and a.receive.buyer.id = ?3 and a.receive.vendor.id=?4 and a.balanceStatus=0  and a.abolished=0")
	List<ReceiveItemEntity> findRecItems(Timestamp begin,Timestamp end,Long buyerId,Long vendorId);
	
	//////////////////////////////通过供货计划查询/////////////////////////////////////
	//通过供货计划id获取aac来料不良数量
	@Query("select sum(ri.zllbl) from ReceiveItemEntity ri where ri.orderItemPlan.id = ?1 and ri.abolished=0")
	public Double getItemPlanZllblQty(Long orderItemPlanId);
	
	//通过供货计划id获取aac质检不良数量
	@Query("select sum(ri.zzjbl) from ReceiveItemEntity ri where ri.orderItemPlan.id = ?1 and ri.abolished=0")
	public Double getItemPlanZzjblQty(Long orderItemPlanId);
	
	//通过供货计划id获取收货数量
	@Query("select sum(ri.receiveQty) from ReceiveItemEntity ri where ri.orderItemPlan.id = ?1 and ri.abolished=0")
	public Double getOrderItemPlanReceiveQty(Long orderItemPlanId);
	//////////////////////////////通过订单明细查询/////////////////////////////////////
	//通过订单行的id获取aac来料不良数量
	@Query("select sum(ri.zllbl) from ReceiveItemEntity ri where ri.orderItem.id = ?1 and ri.abolished=0")
	public Double getOrderItemZllblQty(Long orderItemId);
	
	//通过订单行的id获取aac质检不良数量
	@Query("select sum(ri.zzjbl) from ReceiveItemEntity ri where ri.orderItem.id = ?1 and ri.abolished=0")
	public Double getOrderItemZzjblQty(Long orderItemId);
	
	//通过订单行的id获取aac收货数量
	@Query("select sum(ri.receiveQty) from ReceiveItemEntity ri where ri.orderItem.id = ?1 and ri.abolished=0")
	public Double getOrderItemReceiveQty(Long orderItemId);
	
	//通过计划的id获取aac收货数量[普通+补货]
	@Query("select sum(ri.receiveQty) from ReceiveItemEntity ri where ri.orderItemPlan.id in (?1) and ri.abolished=0")
	public Double getReceiveQtyByPlanIds(List<Long> planIds);
}
