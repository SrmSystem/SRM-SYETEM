package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestItemEntity;


public interface GoodsRequestItemDao extends BaseRepository<GoodsRequestItemEntity, Serializable>,JpaSpecificationExecutor<GoodsRequestItemEntity>{

	@Override
	public List<GoodsRequestItemEntity> findAll();
	
	@Override
	public Page<GoodsRequestItemEntity> findAll(Pageable page);
	
	public List<GoodsRequestItemEntity> findGoodsRequestItemEntityByRequestAndItemNo(GoodsRequestEntity request, Integer itemNo); 
}
