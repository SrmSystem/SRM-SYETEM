package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;


public interface PurchasePlanDao extends BaseRepository<PurchasePlanEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanEntity>{

	@Override
	public List<PurchasePlanEntity> findAll();
	
	@Override
	public Page<PurchasePlanEntity> findAll(Pageable page);

	@Query("select a from PurchasePlanEntity a where a.month = ?1 and a.buyer.code = ?2") 
	public PurchasePlanEntity findPurchasePlanEntityByMonthAndBuyer(String month, String buyerCode);
}
