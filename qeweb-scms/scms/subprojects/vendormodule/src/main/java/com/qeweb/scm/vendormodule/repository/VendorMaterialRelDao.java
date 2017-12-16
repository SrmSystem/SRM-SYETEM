package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;

/**
 * 物料关系Dao
 * @author lw
 * 创建时间：2015年6月15日09:24:37
 * 最后更新时间：2015年6月15日09:24:40
 * 最后更新人：lw
 */
public interface VendorMaterialRelDao extends PagingAndSortingRepository<VendorMaterialRelEntity, Serializable>,JpaSpecificationExecutor<VendorMaterialRelEntity>{
	@Override
	public Iterable<VendorMaterialRelEntity> findAll();
	
	public List<VendorMaterialRelEntity> findByVendorId(Long vendorId); 
	
	@Query("from VendorMaterialRelEntity a where a.material.code = ?1")
	public List<VendorMaterialRelEntity> findByMaterialCode(String materialCode); 
	
	@Override
	public List<VendorMaterialRelEntity> findAll(Specification<VendorMaterialRelEntity> spec);

	public VendorMaterialRelEntity findByOrgIdAndMaterialId(Long orgId, Long materialId);
	@Query(value="SELECT sum(ms.SUPPLY_COEFFICIENT) as mssum FROM(SELECT m.*, s.SUPPLY_COEFFICIENT FROM QEWEB_VENDOR_MATERIAL_REL m LEFT JOIN QEWEB_VENDOR_MAT_SUPPLY_REL s ON m. ID = s.MATERIAL_REL_ID WHERE m.status=1)ms  group by ms.MATERIAL_ID having(sum(ms.SUPPLY_COEFFICIENT)>1)",nativeQuery=true)
	public String getfindvrl();

	public List<VendorMaterialRelEntity> findByVendorIdAndMaterialIdAndStatus(
			Long vendorId, Long materialId,Integer status);

	public List<VendorMaterialRelEntity> findByOrgId(Long orgId);

	public List<VendorMaterialRelEntity> findByMaterialCodeAndStatus(
			String materialCode, int i);
	
	public VendorMaterialRelEntity findByVendorIdAndMaterialId(Long vendorId, Long materialId);
}
