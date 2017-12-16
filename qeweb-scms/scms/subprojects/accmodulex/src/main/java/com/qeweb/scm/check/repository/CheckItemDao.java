package com.qeweb.scm.check.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.CheckItemEntity;


public interface CheckItemDao extends BaseRepository<CheckItemEntity, Serializable>,JpaSpecificationExecutor<CheckItemEntity>{

	/**
	 * 根据收货获取对账明细
	 * @param ckId
	 * @param recItem
	 * @param source
	 * @return
	 */
	@Query("from CheckItemEntity where check.id = ?1 and recItem.id = ?2 and source=?3 and abolished = 0")
	public List<CheckItemEntity> findCheckItemByRec(Long ckId, Long sourceId,int source);
	
	/**
	 * 根据退货获取对账明细
	 * @param ckId
	 * @param retItem
	 * @param source
	 * @return
	 */
	@Query("from CheckItemEntity where check.id = ?1 and retItem.id = ?2 and source=?3 and abolished = 0")
	public List<CheckItemEntity> findCheckItemByRet(Long ckId, Long sourceId,int source);
	
	@Query("select a.id from CheckItemEntity a where a.check.buyer.id in ?1")
	public List<Long> findCheckItemIdsByBuyerId(List<Long> buyerIds );
	
	CheckItemEntity findById(Long checkItemId);
}
