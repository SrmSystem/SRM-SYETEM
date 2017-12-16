package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemMatEntity;


public interface PurchaseOrderItemMatDao extends BaseRepository<PurchaseOrderItemMatEntity, Serializable>,JpaSpecificationExecutor<PurchaseOrderItemMatEntity>{

	List<PurchaseOrderItemMatEntity> findByOrderItemIdAndAbolished(long orderItemId,Integer abolished);
}
