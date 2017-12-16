package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorCapacityRelEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanVendorEntity;


public interface PurchasePlanVendorCapacityRelDao extends BaseRepository<PurchasePlanVendorCapacityRelEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanVendorCapacityRelEntity>{

	@Override
	public List<PurchasePlanVendorCapacityRelEntity> findAll();
	
	@Query("select a from PurchasePlanVendorCapacityRelEntity a where a.vendor.id = ?1")
	public PurchasePlanVendorCapacityRelEntity findOneByVendorId(long vendorId);

}
