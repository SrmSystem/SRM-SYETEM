package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderEntity;


public interface PurchaseOrderDao extends BaseRepository<PurchaseOrderEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderEntity>{

	@Override
	public List<PurchaseOrderEntity> findAll();
	
	@Override
	public Page<PurchaseOrderEntity> findAll(Pageable page);
	
	public List<PurchaseOrderEntity> findByIdIn(Set<Long> ids);

	@Query("from PurchaseOrderEntity a where a.orderCode = ?1 and a.buyer.code = ?2 and a.vendor.code = ?3 and a.abolished=0")
	public PurchaseOrderEntity findPurchaseOrderEntityByCode(String orderCode, String buyerCode, String  vendorCode);

	@Query("from PurchaseOrderEntity a where a.orderCode = ?1 and a.version = ?2 and a.vendor.id = ?3")
	public PurchaseOrderEntity findPurchaseOrderEntityByOrderCodeAndVersionAndVendorId(String orderCode, String version, long verdorId);
	
	@Query("from PurchaseOrderEntity a where a.publishStatus = ?1 and a.orderType=?2")
	public List<PurchaseOrderEntity> findAllByPublishStatusAndOrderType(Integer publishStatus,Integer orderType);

	@Query("from PurchaseOrderEntity a where a.orderCode = ?1")
	public PurchaseOrderEntity getOrderByCode(String poNumber);
	
	@Query("from PurchaseOrderEntity a where a.orderCode = ?1 and a.col2 = ?2")
	public PurchaseOrderEntity getOrderByCodeAndOrgId(String poNumber,String orgId);

	@Query("from PurchaseOrderEntity a where a.orderCode = ?1 and a.version = ?2 and a.vendor.id = ?3 and a.orderType = ?4")
	public PurchaseOrderEntity findPurchaseOrderEntityByOrderCodeAndVersionAndVendorIdAndOrderType(String orderCode, String version, long vendorId, int orderType);
	
	@Query("from PurchaseOrderEntity a where a.orderCode = ?1 and a.vendor.id = ?2 and a.orderType = ?3 order by version desc")
	public List<PurchaseOrderEntity> findPurchaseOrderEntityByOrderCodeAndVendorIdAndOrderType(String orderCode, long vendorId, int orderType);

	public List<PurchaseOrderEntity> findByOrderCode(String code);

	public PurchaseOrderEntity findById(Long id);
}
