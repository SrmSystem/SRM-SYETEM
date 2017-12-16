package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.GoodsRequestEntity;


public interface GoodsRequestDao extends BaseRepository<GoodsRequestEntity, Serializable>,JpaSpecificationExecutor<GoodsRequestEntity>{

	@Override
	public List<GoodsRequestEntity> findAll();
	
	@Override
	public Page<GoodsRequestEntity> findAll(Pageable page);

	@Query("from GoodsRequestEntity a where a.requestCode = ?1 and a.buyer.code = ?2 and a.vendor.code = ?3")
	public GoodsRequestEntity findGoodsRequestEntityByCode(String orderCode, String buyerCode, String  vendorCode);
}
