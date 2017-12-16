package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;


public interface PurchasePlanVendorDao extends BaseRepository<PurchasePlanVendorEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanVendorEntity>{

	@Override
	public List<PurchasePlanVendorEntity> findAll();
	
	@Override
	public Page<PurchasePlanVendorEntity> findAll(Pageable page);
	
	@Query("select a from PurchasePlanVendorEntity a where a.plan.id = ?1 and a.vendor.code = ?2")
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMain(long planId, String vendorCode);
	
	@Query("select a from PurchasePlanVendorEntity a where a.plan.id = ?1 and a.vendor.id = ?2")
	public PurchasePlanVendorEntity findPurchasePlanVendorEntityByMain(long planId, long vendorId);
}
