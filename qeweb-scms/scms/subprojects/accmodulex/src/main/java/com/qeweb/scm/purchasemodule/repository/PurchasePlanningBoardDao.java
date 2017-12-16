package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanningBoardEntity;


public interface PurchasePlanningBoardDao extends BaseRepository<PurchasePlanningBoardEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanningBoardEntity>{

	@Query("from PurchasePlanningBoardEntity where materialCode=?1 and factoryCode=?2 and state=0")
	List<PurchasePlanningBoardEntity> getListDtl(String materialCode,String factoryCode);

	@Modifying
	@Query("update PurchasePlanningBoardEntity set state=1")
	void updateState();
	
}
