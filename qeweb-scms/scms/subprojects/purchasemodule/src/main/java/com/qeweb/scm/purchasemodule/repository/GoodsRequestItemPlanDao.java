package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemEntity;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemPlanEntity;


public interface GoodsRequestItemPlanDao extends BaseRepository<GoodsRequestItemPlanEntity, Serializable>,JpaSpecificationExecutor<GoodsRequestItemPlanEntity>{

	@Override
	public List<GoodsRequestItemPlanEntity> findAll();
	
	public List<GoodsRequestItemPlanEntity> findByIdIn(List<Long> ids);
	
	@Override
	public Page<GoodsRequestItemPlanEntity> findAll(Pageable page);

	public List<GoodsRequestItemPlanEntity> findGoodsRequestItemPlanEntityByRequestItem(GoodsRequestItemEntity item);

	@Query("from GoodsRequestItemPlanEntity a where a.id = ?1")
	public GoodsRequestItemPlanEntity getPlanById(Long requestItemPlanId);

}
