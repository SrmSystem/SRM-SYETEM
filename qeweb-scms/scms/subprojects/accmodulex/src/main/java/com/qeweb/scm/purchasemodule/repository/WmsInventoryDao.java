package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.WmsInventoryEntity;


public interface WmsInventoryDao extends BaseRepository<WmsInventoryEntity, Serializable>,JpaSpecificationExecutor<WmsInventoryEntity>{

	@Override
	public List<WmsInventoryEntity> findAll();
	
	@Override
	public Page<WmsInventoryEntity> findAll(Pageable page);
	
	@Query("from WmsInventoryEntity a where a.material.code = ?1")
	public List<WmsInventoryEntity> findInventoryEntityByMaterial(String materialCode);
	
	@Query(value="select sum(stock_qty) from qeweb_wms_inventory where material_id = ?1",nativeQuery=true)
	public BigDecimal findStockQtyByMaterial(Long id);

	@Query("from WmsInventoryEntity a where a.material.id = ?1")
	public List<WmsInventoryEntity> getWmsInventoryByMaterial(long materialId);
}
