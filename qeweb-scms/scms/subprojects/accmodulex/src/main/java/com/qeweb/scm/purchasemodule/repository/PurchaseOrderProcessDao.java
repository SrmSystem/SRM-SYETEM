package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.ProcessMaterialRelEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderProcessEntity;


public interface PurchaseOrderProcessDao extends BaseRepository<PurchaseOrderProcessEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderProcessEntity>{

	List<PurchaseOrderProcessEntity> findByOrderItemIdAndAbolished(long orderItemId,Integer abolished);
}
