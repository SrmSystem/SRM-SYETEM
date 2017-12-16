package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.MinInventoryEntity;

/**
 * 最小库存DAO
 * @author ALEX
 *
 */
public interface MinInventoryDao extends BaseRepository<MinInventoryEntity, Serializable>,JpaSpecificationExecutor<MinInventoryEntity>{

	@Override
	public List<MinInventoryEntity> findAll();
	
	@Override
	public Page<MinInventoryEntity> findAll(Pageable page);

	@Query("from MinInventoryEntity a where a.vendor.code = ?1 and a.material.code = ?2")
	public List<MinInventoryEntity> findMinInventoryEntityByVendorAndMaterial(String vendorCode, String materialCode);
}