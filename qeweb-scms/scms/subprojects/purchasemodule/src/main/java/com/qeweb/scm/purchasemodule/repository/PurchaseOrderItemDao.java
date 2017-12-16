package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;


public interface PurchaseOrderItemDao extends BaseRepository<PurchaseOrderItemEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderItemEntity>{

	@Override
	public List<PurchaseOrderItemEntity> findAll();
	
	@Query(value="select distinct o.order_code from qeweb_purchase_order_item oi left join qeweb_purchase_order o  on o.id=oi.order_id",nativeQuery=true)
	public List getDistinct();
	
	@Query(value="select distinct o.code from qeweb_purchase_order_item oi left join(SELECT po.*,qo.CODE  FROM qeweb_purchase_order po left join QEWEB_ORGANIZATION qo on po.VENDOR_ID=qo.ID) o  on o.id=oi.order_id",nativeQuery=true)
	public List getEnableOrgCode();
	
	@Query(value="select distinct o.order_code from qeweb_purchase_order_item oi left join qeweb_purchase_order o  on o.id=oi.order_id and o.order_type=?1 and o.buyer_id=?2",nativeQuery=true)
	public List<?> getEnableB(Integer type, Long orgId);
	
	@Query(value="select distinct o.order_code from qeweb_purchase_order_item oi left join qeweb_purchase_order o  on o.id=oi.order_id and o.order_type=?1 and o.vendor_id=?2",nativeQuery=true)
	public List<?> getEnableV(Integer type, Long orgId);
	
	@Query(value="select distinct oi.version from qeweb_purchase_order_item oi RIGHT join qeweb_purchase_order o  on o.id=oi.order_id and o.order_type=?1 and o.order_code=?2  ORDER BY oi.version DESC",nativeQuery=true)
	public List getEnableVersion(Integer type,String code);
	
	@Query(value="select distinct oi.version from qeweb_purchase_order_item oi RIGHT join qeweb_purchase_order o  on o.id=oi.order_id and o.order_code=?1 ORDER BY oi.version DESC",nativeQuery=true)
	public List getEnableVersion(String code);
	
	@Query(value="select distinct oi.code from (select dd.*,m.code from qeweb_purchase_order_item dd left join qeweb_material m on dd.material_id=m.id ) oi RIGHT join (SELECT po.* FROM qeweb_purchase_order po RIGHT join QEWEB_ORGANIZATION qo on po.VENDOR_ID=qo.ID and qo.code=?1) o  on o.id=oi.order_id",nativeQuery=true)
	public List<?> getEnableMaterialCode(String code);
	
	@Query(value="select distinct oi.code from (select dd.*,m.code from (select * from qeweb_purchase_order_item where version=?1) dd left join qeweb_material m on dd.material_id=m.id ) oi RIGHT join qeweb_purchase_order o  on o.id=oi.order_id",nativeQuery=true)
	public List<?> getEnableMaterialCodeV(String code);
	
	@Query(value="select distinct o.code from qeweb_purchase_order_item oi left join(SELECT po.*,qo.CODE  FROM qeweb_purchase_order po left join QEWEB_ORGANIZATION qo on po.VENDOR_ID=qo.ID) o  on o.id=oi.order_id and o.order_type=?1",nativeQuery=true)
	public List getEnableOrgCode(Integer type);
	
	@Query(value="select distinct oi.code from (select dd.*,m.code from (select * from qeweb_purchase_order_item) dd left join qeweb_material m on dd.material_id=m.id ) oi RIGHT join (SELECT po.* FROM qeweb_purchase_order po RIGHT join QEWEB_ORGANIZATION qo on po.VENDOR_ID=qo.ID and qo.code=?1) o  on o.id=oi.order_id and o.order_type=?2",nativeQuery=true)
	public List<?> getEnableMaterialCode(String code,Integer type);
	
	@Query(value="select distinct oi.code from (select dd.*,m.code from (select * from qeweb_purchase_order_item where version=?1) dd left join qeweb_material m on dd.material_id=m.id ) oi RIGHT join qeweb_purchase_order o  on o.id=oi.order_id  and o.order_code=?2 and o.order_type=?3",nativeQuery=true)
	public List<?> getEnableMaterialCodeV(String code,String code1,Integer type);
	
	@Override
	public Page<PurchaseOrderItemEntity> findAll(Pageable page);
	
	public List<PurchaseOrderItemEntity> findByIdIn(Set<Long> ids);
	
	@Query("from PurchaseOrderItemEntity a where a.order.id = ?1 and a.itemNo = ?2")
	public PurchaseOrderItemEntity findPurchaseOrderItemEntityByMain(long orderId, Integer itemNo);

	public List<PurchaseOrderItemEntity> findPurchaseOrderItemEntityByOrder(PurchaseOrderEntity order);

	@Query("from PurchaseOrderItemEntity a where a.itemNo = ?1 and a.material.id = ?2")
	public PurchaseOrderItemEntity findPurchaseOrderItemEntityByItemNoAndMaterialId(int itemNo, long materialId); 
	
	@Query("from PurchaseOrderItemEntity a where a.order.orderType=1 and a.order.orderCode = ?1 and a.order.version = ?2 and a.order.vendor.code = ?3 and a.itemNo = ?4 and a.material.code = ?5 and a.orderStatus=1 and a.publishStatus=0 order by a.requestTime asc")
	public List<PurchaseOrderItemEntity> findItemByParams(String orderCode, String version, String vendorCode, Integer itemNo, String materialCode);
	
	@Query("from PurchaseOrderItemEntity a where a.order.orderType=1 and a.id= ?1 and a.order.orderCode = ?2 and a.order.version = ?3 and a.order.vendor.code = ?4 and a.itemNo = ?5 and a.material.code = ?6 and a.orderStatus=1 and a.publishStatus=0 order by a.requestTime asc")
	public List<PurchaseOrderItemEntity> findItemsByParams(Long itemId,String orderCode, String version, String vendorCode, Integer itemNo, String materialCode);
	
	@Query(value="select sum(oi.order_qty) from qeweb_purchase_order o inner join qeweb_purchase_order_item oi on o.id=oi.order_id where o.vendor_id=?1 and oi.material_id=?2",nativeQuery=true)
	public BigDecimal findOrderQtyByVendorAndMaterial(Long vendorId,Long materialId);

	@Query("from PurchaseOrderItemEntity a where a.deliveryStatus = 1 and a.col1 is null or a.col1 <> 'true' ")
	public List<PurchaseOrderItemEntity> findByCol1IsNullOrCol1Not();

	//add by zhangjiejun 2015.10.15 start
	/**
	 * 通过订单id，行号和零件id定位item对象
	 * @param 	order		订单对象
	 * @param 	itemNo		行号
	 * @param 	material	零件对象
	 * @return	item对象
	 */
	public PurchaseOrderItemEntity findPurchaseOrderItemEntityByOrderAndItemNoAndMaterial(PurchaseOrderEntity order, int itemNo, MaterialEntity material);
	//add by zhangjiejun 2015.10.15 end

	//add by zhangjiejun 2015.10.23 start
	/**
	 * 通过队列号查询订单明细对象
	 * @param 	string	队列号
	 * @return	订单明细对象
	 */
	public PurchaseOrderItemEntity findByCol12(String string);
	//add by zhangjiejun 2015.10.23 end

	//add by zhangjiejun 2015.11.16 start
	@Query("from PurchaseOrderItemEntity a where a.order.vendor.id = ?1 and a.order.orderCode = ?2 and a.version = ?3 and a.itemNo = ?4 and a.unitName = ?5 and a.col3 = ?6 and a.orderStatus = ?7 and a.orderQty = ?8 and a.requestTime = ?9")
	public PurchaseOrderItemEntity findByConditions(long vendorId, String orderCode, String version, int itemNo,
			String unitName, String col3, Integer orderStatus, Double orderQty, Timestamp requestTime);
	//add by zhangjiejun 2015.11.16 end

	//add by zhangjiejun 2016.01.25 start   //modify by chao.gu 20160603 由原先的有无订单改为有无交付 实际收货时间来确定那周是否有无交付 已写人receeiveDao
	//@Query("from PurchaseOrderItemEntity a where a.order.vendor.id = ?1 and a.confirmTime >= ?2 and a.confirmTime <= ?3")
	//public List<PurchaseOrderItemEntity> findByVendorIdAndTimes(long orgId, Timestamp startTime, Timestamp endTime);
	//add by zhangjiejun 2016.01.25 end

	//add by zhangjiejun 2016.02.18 start
	@Query("from PurchaseOrderItemEntity a where a.order.vendor.id = ?1 and a.order.orderCode = ?2 and a.version = ?3 and a.itemNo = ?4 and a.unitName = ?5 and a.col3 = ?6 and a.orderStatus = ?7 and a.requestTime = ?8")
	public PurchaseOrderItemEntity findByConditions2(long vendorId, String orderCode, String version, int itemNo,
			String unitName, String col3, Integer orderStatus, Timestamp requestTime);
	//add by zhangjiejun 2016.02.18 end
	
}
