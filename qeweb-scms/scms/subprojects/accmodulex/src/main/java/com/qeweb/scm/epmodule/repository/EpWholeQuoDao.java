package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpWholeQuoEntity;

/**
 * 整项报价DAO
 * @author ronnie
 *
 */
public interface EpWholeQuoDao extends BaseRepository<EpWholeQuoEntity, Serializable>,JpaSpecificationExecutor<EpWholeQuoEntity>{

	@Query("from EpWholeQuoEntity a where a.epPrice.id = ?1 And a.epVendor.id = ?2 And a.epMaterial.id = ?3 And a.isVendor = ?4")
	EpWholeQuoEntity findByEpPriceAndEpVendorAndEpMaterial(Long priceId,Long epVendorId,Long epMaterialId,Integer isVendor);
	
	@Query("from EpWholeQuoEntity a where a.epPrice.id = ?1 And a.epMaterial.id = ?2 And a.isVendor = ?3")
	List<EpWholeQuoEntity> findByEpPriceAndEpMaterial(Long priceId,Long epMaterialId,Integer isVendor);
	
	@Query("from EpWholeQuoEntity a where a.epVendor.vendorId = ?1 And a.epMaterial.id = ?2 And a.isVendor = ?3")
	EpWholeQuoEntity findByEpPriceAndVendorAndEpMaterial(Long vendorId,Long epMaterialId,Integer isVendor);
	
	@Query("from EpWholeQuoEntity a where a.epPrice.id = ?1")
	List<EpWholeQuoEntity> findByEpPrice(Long epPriceId);
	
	@Query("from EpWholeQuoEntity a where a.epVendor.id = ?1")
	List<EpWholeQuoEntity> findWholeByVendorId(Long epVendorId);
	
	//add by yao.jin
	//根据供应商id、物料id查找报价单
	@Query("from EpWholeQuoEntity a where a.epVendor.vendorId = ?1 And a.epMaterial.material.id = ?2")
	List<EpWholeQuoEntity> findByVendorAndMaterial(Long vendorId,Long materialId);
	
	//end add
}
