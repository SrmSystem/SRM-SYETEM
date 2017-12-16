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
import com.qeweb.scm.purchasemodule.entity.ReceiveEntity;


public interface ReceiveDao extends BaseRepository<ReceiveEntity, Serializable>,JpaSpecificationExecutor<ReceiveEntity>{

	@Override
	public List<ReceiveEntity> findAll();
	
	@Override
	public Page<ReceiveEntity> findAll(Pageable page);

	@Query("from ReceiveEntity a where a.receiveCode = ?1 and a.buyer.code = ?2 and a.vendor.code= ?3")
	public ReceiveEntity getReceiveEntityByCode(String receiveCode, String buyerCode, String vendorCode);

	@Query("from ReceiveEntity a where a.receiveCode = ?1")
	public ReceiveEntity getReceiveByCode(String receiptNumber);

	@Query("from ReceiveEntity a where a.receiveCode = ?1 and a.delivery.order.col2 = ?2")
	public ReceiveEntity getReceiveByCodeAndOrgId(String receiptNumber, String orgId);
	
	@Query("from ReceiveEntity a where a.attr7 = ?1 and a.delivery.order.col2 = ?2")
	public ReceiveEntity getReceiveByPoNumberAndOrgId(String poNumber, String orgId);
	
	@Query("from ReceiveEntity a where a.attr7 = ?1")
	public ReceiveEntity getReceiveByPoNumber(String poNumber);

	public List<ReceiveEntity> findByDelivery(DeliveryEntity deliveryEntity);

	//add by zhangjiejun 2016.1.26 start
	
  //modify by chao.gu 20160603 由原先的有无订单改为有无交付 实际收货时间来确定那周是否有无交付
		@Query("from ReceiveEntity a where a.vendor.id = ?1 and a.receiveTime >= ?2 and a.receiveTime <= ?3")
		public List<ReceiveEntity> findByVendorIdAndTimes(long orgId, Timestamp startTime, Timestamp endTime);
		//add by zhangjiejun 2016.01.25 end
	/**
	 * 通过收货单号查询收货对象
	 * @param receiveCode	收货单号
	 * @return				收货对象
	 */
	public ReceiveEntity findByReceiveCode(String receiveCode);
	//add by zhangjiejun 2016.1.26 end
}
