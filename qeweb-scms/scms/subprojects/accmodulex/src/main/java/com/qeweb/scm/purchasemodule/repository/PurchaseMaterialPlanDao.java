package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseMaterialPlanEntity;


public interface PurchaseMaterialPlanDao extends BaseRepository<PurchaseMaterialPlanEntity, Serializable>,JpaSpecificationExecutor<PurchaseMaterialPlanEntity>{
	
	PurchaseMaterialPlanEntity findByMaterialIdAndPlanTime(long materialId,Timestamp planTime);
}
