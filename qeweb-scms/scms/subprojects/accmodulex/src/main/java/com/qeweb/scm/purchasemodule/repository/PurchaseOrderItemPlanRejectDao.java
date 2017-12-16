package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanRejectEntity;


public interface PurchaseOrderItemPlanRejectDao extends BaseRepository<PurchaseOrderItemPlanRejectEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderItemPlanRejectEntity>{

	@Override
	public List<PurchaseOrderItemPlanRejectEntity> findAll();
}
