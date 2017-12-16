package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;


public interface PurchasePlanVendorDao extends BaseRepository<PurchasePlanVendorEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanVendorEntity>{

	@Override
	public List<PurchasePlanVendorEntity> findAll();
	
	public List<PurchasePlanVendorEntity> findByPlanId(Long planId);
	
	@Override
	public Page<PurchasePlanVendorEntity> findAll(Pageable page);
	
	@Query("select a from PurchasePlanVendorEntity a where a.plan.id = ?1 and a.vendor.code = ?2 and a.abolished=0")
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMain(long planId, String vendorCode);
	
	@Query("select a from PurchasePlanVendorEntity a where a.plan.id = ?1 and a.vendor.id = ?2")
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMain(long planId, long vendorId);
	
	
	@Query("select a from PurchasePlanVendorEntity a where a.month = ?1 and a.vendor.code = ?2 and a.abolished=0") 
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMonthAndVendor(String month, String vendorCode);
	
	@Query("select a from PurchasePlanVendorEntity a where a.month = ?1 and a.vendor.id = ?2 and  a.group.id = ?3 and a.abolished=0") 
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMonthAndVendorAndGroup(String month, Long vendorId,Long buyerId);
	
	
	@Query("select a from PurchasePlanVendorEntity a where a.plan.id = ?1 and a.vendor.id = ?2 and a.abolished=0") 
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByVendorAndVendorId(Long id,Long vendorId);
	
	/**
	 * 根据plan的ID列表获取供应商大版本ID(待办用)
	 * @author chao.gu
	 * @param itemIds
	 * @return
	 */
	@Query("select distinct(a.id) from PurchasePlanVendorEntity a where   a.plan.id in(?1) and a.vendor.id=?2 and a.abolished=0")
	public List<Long> findPurchasePlanVendorIdsByPlanIds(List<Long> planIds,Long vendorId);
	
	public PurchasePlanVendorEntity findById(Long id);
}
