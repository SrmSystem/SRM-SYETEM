package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.BuyerBoardEntity;


public interface BuyerBoardDao extends BaseRepository<BuyerBoardEntity, Serializable>,JpaSpecificationExecutor<BuyerBoardEntity>{

	@Query("from BuyerBoardEntity where materialCode=?1 and factoryCode=?2 and state=0")
	List<BuyerBoardEntity> getListDtl(String materialCode, String factoryCode);

	@Modifying
	@Query("update BuyerBoardEntity set state=1")
	void updateState();
}
