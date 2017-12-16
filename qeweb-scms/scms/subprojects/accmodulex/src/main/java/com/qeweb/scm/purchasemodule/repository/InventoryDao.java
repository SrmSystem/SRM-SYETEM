package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.InventoryEntity;


public interface InventoryDao extends BaseRepository<InventoryEntity, Serializable>,JpaSpecificationExecutor<InventoryEntity>{

	@Override
	public List<InventoryEntity> findAll();
	
	@Override
	public Page<InventoryEntity> findAll(Pageable page);
	
	@Query("from InventoryEntity a where a.vendor.code = ?1 and a.material.code = ?2")
	public List<InventoryEntity> findInventoryEntityByVendorAndMaterial(String vendorCode, String materialCode);
	
	@Query(value="select sum(stock_qty) from qeweb_inventory where material_id = ?1",nativeQuery=true)
	public BigDecimal findStockQtyByMaterial(Long id);
	
	
	@Query("from InventoryEntity a where a.material.id = ?1")
	public List<InventoryEntity> findInventoryEntityByMaterialId(Long materialId);
	
	@Query("from InventoryEntity a where a.vendor.id=?1 and a.material.id = ?2")
	public List<InventoryEntity> findInventoryEntityByVendorIdAndMaterialId(Long vendorId,Long materialId);
	
	@Query("from InventoryEntity a where a.store.col2=?1 and a.vendor.id=?2 and a.material.id=?3")
	public List<InventoryEntity> findInventoryEntityByStore(String storeCol2,Long vendorId,Long materialId);

	@Query("from InventoryEntity a where (a.stockQty+a.passageQty)<a.rop")
	public List<InventoryEntity> findInventoryEntity();

	@Query("from InventoryEntity a where a.vendor.id = ?1 and a.material.id = ?2")
	public List<InventoryEntity> getInventoryByVendorAndMaterial(Long vendorId, Long materialId);
}
