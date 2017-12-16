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


public interface DeliveryDao extends BaseRepository<DeliveryEntity, Serializable>,JpaSpecificationExecutor<DeliveryEntity>{

	@Override
	public List<DeliveryEntity> findAll();
	
	@Override
	public Page<DeliveryEntity> findAll(Pageable page);
	
	
	@Query("from DeliveryEntity a where a.deliveryCode = ?1")
	public DeliveryEntity findDeliveryEntityByCode(String deliveryCode);
	
	@Query("from DeliveryEntity a where a.order.orderCode = ?1")
	public DeliveryEntity findDeliveryEntityByOrderCode(String orderCode);
	
	@Query("from DeliveryEntity a where a.order.orderCode = ?1 and a.order.col2 = ?2")
	public DeliveryEntity findDeliveryEntityByOrderCodeAndOrgId(String orderCode,String orgId);

	@Query("from DeliveryItemEntity a where a.delivery.deliveryCode = ?1 and a.material.code = ?2")
	public DeliveryItemEntity getDeliveryItem(long id, String itemCode);
	
	@Query("from DeliveryItemEntity a where a.delivery.order.orderCode = ?1 and a.material.code = ?2")
	public DeliveryItemEntity getDeliveryItemByCode(String poNumber, String itemCode);
	
	@Query("from DeliveryItemEntity a where a.delivery.order.orderCode = ?1 and a.delivery.order.col2 = ?3 and a.material.code = ?2")
	public List<DeliveryItemEntity> getDeliveryItemByCodeAndOrgId(String poNumber, String itemCode, String orgId);
	
	@Query("from DeliveryEntity a where a.deliveryCode = ?1 and a.deliveryType =?2")
	public DeliveryEntity findDeliveryEntityByCodeAndType(String deliveryCode,Integer delivertType);
	
	//add by zhangjiejun 2015.10.26 start
	/**
	 * 根据发货明细id和类型获取结果集
	 * @param id	发货明细id
	 * @param type	类型
	 * @return		结果集
	 */
	@Query(value="select ri.* from qeweb_receive_item ri "
			+ "inner join qeweb_receive r on r.id = ri.receive_id and ri.attr11 = ?2 and ri.delivery_item_id = ?1 "
			+ "inner join qeweb_delivery_item di on di.id = ri.delivery_item_id "
			+ "inner join  qeweb_delivery d on d.id = r.delivery_id",nativeQuery=true)
	public Object[] findReceiveItemByDeliveryItemIdAndType(long id, String type);
	//add by zhangjiejun 2015.10.26 end
	
	@Query(value="select distinct o.code from qeweb_delivery d left join QEWEB_ORGANIZATION o on d.vendor_id=o.id and d.delivery_Type=?1 and  d.buyer_id=?2",nativeQuery=true)
	public List<?> getEnableOrgCode(Integer type,Long orgId);
	
	@Query(value="select distinct o.code from qeweb_delivery d left join QEWEB_ORGANIZATION o on d.vendor_id=o.id and d.delivery_Type=?1 and  d.buyer_id=?2",nativeQuery=true)
	public List<?> getEnableOrgCode2(Integer type,Long orgId);
}
